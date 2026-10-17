package hu.bsstudio.bssweb.video.page

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.label.model.Label
import hu.bsstudio.bssweb.label.service.LabelService
import hu.bsstudio.bssweb.media.service.MediaService
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.SimpleMember
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.ui.config.UiSecurityConfig
import hu.bsstudio.bssweb.ui.flash.Flash
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(VideoPageController::class)
@Import(UiSecurityConfig::class)
internal class VideoPageControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var videoService: VideoService

    @MockkBean
    private lateinit var labelService: LabelService

    @MockkBean
    private lateinit var memberService: MemberService

    @MockkBean
    private lateinit var videoCrewService: VideoCrewService

    @MockkBean
    private lateinit var mediaService: MediaService

    @BeforeEach
    internal fun setUp() {
        every { videoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)
        every { labelService.findAllLabels() } returns listOf(LABEL)
        every { memberService.findAllMembers() } returns listOf(MEMBER)
        every { videoCrewService.getPositions() } returns listOf(POSITION)
    }

    @Test
    internal fun `should list videos newest first, then by title`() {
        val pageable = slot<Pageable>()
        every { videoService.findAllVideos(capture(pageable)) } returns PageImpl(listOf(VIDEO), PageRequest.of(0, 25), 1)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/video/list") }
                content { string(containsString(TITLE)) }
                content { string(containsString("""hx-put="$BASE_URL/$VIDEO_ID/visibility?visible=false"""")) }
                content { string(containsString("<span>1–1 of 1 item</span>")) }
                content { string(containsString("""srcset="http://localhost:8890/v/$VIDEO_ID/thumbnail/sd.webp"""")) }
            }

        pageable.captured.pageSize shouldBe 25
        pageable.captured.sort
            .getOrderFor("shootingDateStart")
            ?.direction shouldBe Sort.Direction.DESC
    }

    @Test
    internal fun `should change visibility of one video and return only its cell`() {
        every { videoService.changeVideoVisibility(listOf(VIDEO_ID), false) } returns listOf(VIDEO_ID)
        every { videoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO.copy(visible = false))

        mockMvc
            .put("$BASE_URL/$VIDEO_ID/visibility") {
                with(oauth2Login())
                with(csrf())
                param("visible", "false")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""hx-put="$BASE_URL/$VIDEO_ID/visibility?visible=true"""")) }
                content { string(containsString("""id="visible-$VIDEO_ID"""")) }
                content { string(not(containsString("<html"))) }
            }
    }

    @Test
    internal fun `should publish selected videos`() {
        every { videoService.changeVideoVisibility(listOf(VIDEO_ID), true) } returns listOf(VIDEO_ID)

        mockMvc
            .post("$BASE_URL/visibility") {
                with(oauth2Login())
                with(csrf())
                param("videoIds", VIDEO_ID.toString())
                param("visible", "true")
                param("page", "2")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL?page=2")
                flash { attribute("flash", Flash("video.flash.published", 1)) }
            }
    }

    @Test
    internal fun `should hide selected videos`() {
        every { videoService.changeVideoVisibility(listOf(VIDEO_ID), false) } returns listOf(VIDEO_ID)

        mockMvc
            .post("$BASE_URL/visibility") {
                with(oauth2Login())
                with(csrf())
                param("videoIds", VIDEO_ID.toString())
                param("visible", "false")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL?page=0")
                flash { attribute("flash", Flash("video.flash.hidden", 1)) }
            }
    }

    @Test
    internal fun `should ask for a selection when nothing is selected`() {
        mockMvc
            .post("$BASE_URL/visibility") {
                with(oauth2Login())
                with(csrf())
                param("visible", "true")
            }.andExpectAll {
                status { is3xxRedirection() }
                flash { attribute("flash", Flash("video.flash.noneSelected", error = true)) }
            }
    }

    @Test
    internal fun `should show the new video form`() {
        mockMvc
            .get("$BASE_URL/new") {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/video/new") }
                content { string(containsString("""name="title" value="" required autofocus aria-describedby="title-help form-error">""")) }
            }
    }

    @Test
    internal fun `should create a video and open it`() {
        every { videoService.insertVideo(CreateVideo(TITLE)) } returns VIDEO

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("title", "  $TITLE  ")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL/$VIDEO_ID")
                flash { attribute("flash", Flash("video.flash.created", TITLE)) }
            }
    }

    @Test
    internal fun `should explain a duplicate title when creating`() {
        every { videoService.insertVideo(any()) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
            }.andExpectAll {
                status { isOk() }
                view { name("admin/video/new") }
                content { string(containsString("Another video already uses this title.")) }
                content { string(containsString("""name="title" value="$TITLE"""")) }
                content { string(containsString("""aria-invalid="true"""")) }
            }
    }

    @Test
    internal fun `should say so when the file server fails after creating`() {
        every { videoService.insertVideo(CreateVideo(TITLE)) } throws ResourceAccessException("connection refused")

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("video.flash.createdWithoutFiles", TITLE, error = true)) }
            }
    }

    @Test
    internal fun `should fill the edit form`() {
        mockMvc
            .get("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/video/edit") }
                model { attribute("form", VideoForm.from(DETAILED_VIDEO)) }
                content { string(containsString(">$URL</textarea>")) }
                content { string(containsString("""value="2026-08-13"""")) }
                content { string(containsString(MEMBER.name)) }
                content { string(containsString("""required autofocus aria-describedby="form-error"""")) }
                content { string(containsString("""src="http://localhost:8890/v/$VIDEO_ID/thumbnail/hd.jpeg"""")) }
                content { string(containsString("""alt="Thumbnail of “$TITLE”"""")) }
                content { string(containsString("""data-unsaved="track">""")) }
                content { string(containsString("""<span class="unsaved" role="status">Unsaved changes</span>""")) }
                content { string(not(containsString("""autofocus="autofocus""""))) }
            }
    }

    @Test
    internal fun `should save the form and return to the list`() {
        val update =
            UpdateVideo(
                title = TITLE,
                urls = listOf(URL, "gtb-2026-vam"),
                description = "",
                shootingDateStart = SHOOTING_DATE,
                shootingDateEnd = SHOOTING_DATE.plusDays(1),
                visible = true,
                labels = listOf(LABEL.name),
            )
        every { videoService.updateVideo(VIDEO_ID, update) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .post("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
                with(csrf())
                param("title", " $TITLE ")
                param("urls", "$URL\n\n gtb-2026-vam \n")
                param("description", "")
                param("shootingDateStart", "2026-08-13")
                param("shootingDateEnd", "2026-08-14")
                param("visible", "true")
                param("labels", LABEL.name)
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("video.flash.saved", TITLE)) }
            }

        verify { videoService.updateVideo(VIDEO_ID, update) }
    }

    @Test
    internal fun `should keep the form open when saving conflicts`() {
        every { videoService.updateVideo(VIDEO_ID, any()) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
                param("urls", URL)
                param("description", "")
                param("shootingDateStart", "2026-08-13")
                param("shootingDateEnd", "2026-08-13")
            }.andExpectAll {
                status { isOk() }
                view { name("admin/video/edit") }
                content { string(containsString("Could not save.")) }
                content { string(containsString("""data-unsaved="pending">""")) }
            }
    }

    @Test
    internal fun `should answer not found when saving a deleted video`() {
        every { videoService.updateVideo(VIDEO_ID, any()) } returns Optional.empty()

        mockMvc
            .post("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
                param("urls", URL)
                param("description", "")
                param("shootingDateStart", "2026-08-13")
                param("shootingDateEnd", "2026-08-13")
            }.andExpect {
                status { isNotFound() }
            }
    }

    @Test
    internal fun `should say so when the file server fails after saving`() {
        every { videoService.updateVideo(VIDEO_ID, any()) } throws ResourceAccessException("connection refused")

        mockMvc
            .post("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
                param("urls", "")
                param("description", "")
                param("shootingDateStart", "2026-08-13")
                param("shootingDateEnd", "2026-08-13")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL/$VIDEO_ID")
                flash { attribute("flash", Flash("video.flash.savedWithoutFiles", TITLE, error = true)) }
            }
    }

    @Test
    internal fun `should add a crew member and return only the crew section`() {
        val request = VideoCrewRequest(VIDEO_ID, POSITION, MEMBER.id)
        every { videoCrewService.addPosition(request) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .post("$BASE_URL/$VIDEO_ID/crew") {
                with(oauth2Login())
                with(csrf())
                param("memberId", MEMBER.id.toString())
                param("position", " $POSITION ")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<section id="crew" class="panel">""")) }
                content { string(containsString("""autofocus="autofocus"""")) }
                content { string(not(containsString("<html"))) }
            }

        verify { videoCrewService.addPosition(request) }
    }

    @Test
    internal fun `should remove a crew member`() {
        val request = VideoCrewRequest(VIDEO_ID, POSITION, MEMBER.id)
        every { videoCrewService.removePosition(request) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .delete("$BASE_URL/$VIDEO_ID/crew") {
                with(oauth2Login())
                with(csrf())
                param("memberId", MEMBER.id.toString())
                param("position", POSITION)
            }.andExpect {
                status { isOk() }
            }

        verify { videoCrewService.removePosition(request) }
    }

    @Test
    internal fun `should upload a thumbnail and show the new one`() {
        every { mediaService.uploadVideoThumbnail(VIDEO_ID, IMAGE) } just runs

        mockMvc
            .multipart("$BASE_URL/$VIDEO_ID/thumbnail") {
                with(oauth2Login())
                with(csrf())
                file(IMAGE)
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<section id="video-thumbnail" class="panel">""")) }
                content { string(containsString("Thumbnail uploaded.")) }
                content { string(containsString("/thumbnail/hd.jpeg?v=")) }
                content { string(not(containsString("<html"))) }
            }
    }

    @Test
    internal fun `should explain an image the file server refuses`() {
        every { mediaService.uploadVideoThumbnail(VIDEO_ID, any()) } throws HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)

        mockMvc
            .multipart("$BASE_URL/$VIDEO_ID/thumbnail") {
                with(oauth2Login())
                with(csrf())
                file(IMAGE)
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("The file server couldn’t process this image.")) }
            }
    }

    @Test
    internal fun `should explain that the file server is down`() {
        every { mediaService.uploadVideoThumbnail(VIDEO_ID, any()) } throws ResourceAccessException("connection refused")

        mockMvc
            .multipart("$BASE_URL/$VIDEO_ID/thumbnail") {
                with(oauth2Login())
                with(csrf())
                file(IMAGE)
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("The file server can’t be reached right now.")) }
            }
    }

    @Test
    internal fun `should delete a video`() {
        every { videoService.deleteVideoById(VIDEO_ID) } just runs

        mockMvc
            .post("$BASE_URL/$VIDEO_ID/delete") {
                with(oauth2Login())
                with(csrf())
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("video.flash.deleted", TITLE)) }
            }
    }

    @Test
    internal fun `should keep a video that is still referenced`() {
        every { videoService.deleteVideoById(VIDEO_ID) } throws DataIntegrityViolationException("referenced")

        mockMvc
            .post("$BASE_URL/$VIDEO_ID/delete") {
                with(oauth2Login())
                with(csrf())
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL/$VIDEO_ID")
                flash { attribute("flash", Flash("video.flash.deleteBlocked", TITLE, error = true)) }
            }
    }

    @Test
    internal fun `should answer not found for an unknown video`() {
        val unknownId = UUID.randomUUID()
        every { videoService.findVideoById(unknownId) } returns Optional.empty()

        mockMvc
            .get("$BASE_URL/$unknownId") {
                with(oauth2Login())
            }.andExpect {
                status { isNotFound() }
            }
    }

    private companion object {
        private const val BASE_URL = "/admin/videos"
        private const val TITLE = "Gólyatábor 2026 - Vám"
        private const val URL = "golyatabor-2026-vam"
        private const val POSITION = "Camera"
        private val IMAGE = MockMultipartFile("image", "vam.jpg", "image/jpeg", byteArrayOf(1, 2, 3))
        private val VIDEO_ID = UUID.randomUUID()
        private val SHOOTING_DATE = LocalDate.of(2026, 8, 13)
        private val LABEL = Label(UUID.randomUUID(), "Egyetemi élet", "University life")
        private val MEMBER =
            Member(
                id = UUID.randomUUID(),
                url = "anna",
                name = "Kovács Anna",
                nickname = "",
                description = "",
                joinedAt = SHOOTING_DATE,
                role = "",
                status = MemberStatus.MEMBER,
                archived = false,
            )
        private val VIDEO =
            Video(
                id = VIDEO_ID,
                title = TITLE,
                urls = listOf(URL),
                description = "",
                shootingDateStart = SHOOTING_DATE,
                shootingDateEnd = SHOOTING_DATE,
                visible = true,
            )
        private val DETAILED_VIDEO =
            DetailedVideo(
                id = VIDEO_ID,
                title = TITLE,
                urls = listOf(URL),
                description = "",
                shootingDateStart = SHOOTING_DATE,
                shootingDateEnd = SHOOTING_DATE,
                visible = true,
                labels = listOf(LABEL.name),
                crew = listOf(VideoCrew(VIDEO_ID, POSITION, SimpleMember(MEMBER.id, MEMBER.name, MEMBER.nickname))),
            )
    }
}
