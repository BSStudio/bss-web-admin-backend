package hu.bsstudio.bssweb.video.page

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.label.model.Label
import hu.bsstudio.bssweb.label.service.LabelService
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
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import jakarta.servlet.ServletException
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.startsWith
import org.hamcrest.Matchers.stringContainsInOrder
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
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

    @BeforeEach
    internal fun setUp() {
        every { videoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)
        every { labelService.findAllLabels() } returns listOf(LABEL)
        every { memberService.findAllMembers() } returns listOf(MEMBER)
        every { videoCrewService.getPositions() } returns listOf(POSITION)
    }

    @Test
    internal fun `should send only the table to an htmx request`() {
        every { videoService.findAllVideos(any<Pageable>()) } returns PageImpl(listOf(VIDEO), PageRequest.of(0, 25), 1)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                header("HX-Request", "true")
            }.andExpectAll {
                status { isOk() }
                content { string(startsWith("""<div id="results">""")) }
                content { string(not(containsString("<html"))) }
            }
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
                content {
                    string(
                        containsString("""<span class="cds--pagination__text cds--pagination__items-count">1–1 of 1 item</span>"""),
                    )
                }
                content { string(containsString("""value="25" selected="selected">25</option>""")) }
                content { string(containsString("""selected="selected">1</option>""")) }
                content { string(containsString("""<span class="cds--pagination__text">of 1 page</span>""")) }
                content {
                    string(
                        containsString("""<input type="checkbox" class="cds--checkbox" id="select-all" data-select-all="videoIds">"""),
                    )
                }
                content { string(containsString("""aria-sort="descending">""")) }
                content { string(containsString("""href="/admin/videos?q=&amp;size=25&amp;sort=shootingDateStart,asc"""")) }
                content { string(containsString("""href="/admin/videos?q=&amp;size=25&amp;sort=title,asc"""")) }
            }

        pageable.captured.pageSize shouldBe 25
        pageable.captured.sort shouldBe Sort.by(Sort.Order.desc("shootingDateStart"), Sort.Order.asc("title"))
    }

    @Test
    internal fun `should sort videos by a column, then the default order`() {
        val pageable = slot<Pageable>()
        every { videoService.findAllVideos(capture(pageable)) } returns PageImpl(listOf(VIDEO), PageRequest.of(0, 25), 1)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("sort", "title,desc")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""href="/admin/videos?q=&amp;size=25&amp;sort=title,asc"""")) }
                content { string(containsString("""<input type="hidden" name="sort" value="title,desc">""")) }
            }

        pageable.captured.sort shouldBe Sort.by(Sort.Order.desc("title"), Sort.Order.desc("shootingDateStart"))
    }

    @Test
    internal fun `should ignore a sort by a column the table does not have`() {
        val pageable = slot<Pageable>()
        every { videoService.findAllVideos(capture(pageable)) } returns PageImpl(emptyList(), PageRequest.of(0, 25), 0)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("sort", "description,asc")
            }.andExpect {
                status { isOk() }
            }

        pageable.captured.sort shouldBe Sort.by(Sort.Order.desc("shootingDateStart"), Sort.Order.asc("title"))
    }

    @Test
    internal fun `should search all videos by title and URL, newest first, then by title`() {
        val opening = VIDEO.copy(id = UUID.randomUUID(), title = "Gólyatábor 2026 - Megnyitó", urls = emptyList())
        val older = VIDEO.copy(id = UUID.randomUUID(), title = "Gólyatábor 2025", shootingDateStart = SHOOTING_DATE.minusYears(1))
        val other = VIDEO.copy(id = UUID.randomUUID(), title = "Schönherz Qpa", urls = listOf("qpa"))
        every { videoService.findAllVideos() } returns listOf(older, VIDEO, other, opening)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("q", "GOLYATABOR")
            }.andExpectAll {
                status { isOk() }
                content { string(stringContainsInOrder(opening.title, TITLE, older.title)) }
                content { string(not(containsString(other.title))) }
                content { string(containsString("""type="search" name="q" value="GOLYATABOR"""")) }
                content {
                    string(
                        containsString("""<span class="cds--pagination__text cds--pagination__items-count">1–3 of 3 items</span>"""),
                    )
                }
            }
    }

    @Test
    internal fun `should sort search results by a column`() {
        val older = VIDEO.copy(id = UUID.randomUUID(), title = "Gólyatábor 2025", shootingDateStart = SHOOTING_DATE.minusYears(1))
        val hidden = VIDEO.copy(id = UUID.randomUUID(), title = "Gólyatábor 2026 - Bál", visible = false)
        every { videoService.findAllVideos() } returns listOf(VIDEO, older, hidden)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("q", "golyatabor")
                param("sort", "visible,asc")
            }.andExpect {
                content { string(stringContainsInOrder(hidden.title, TITLE, older.title)) }
            }
    }

    @Test
    internal fun `should say when no video matches the search`() {
        every { videoService.findAllVideos() } returns listOf(VIDEO)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("q", "qpa")
            }.andExpect {
                content { string(containsString("Nothing matches “qpa”.")) }
            }
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
                content { string(containsString("""role="switch"""")) }
                content { string(containsString("""<div hx-swap-oob="beforeend:#toasts">""")) }
                content { string(containsString("“$TITLE” is hidden from the website.")) }
                content { string(not(containsString("<html"))) }
            }
    }

    @Test
    internal fun `should say in a toast that a video is shown`() {
        every { videoService.changeVideoVisibility(listOf(VIDEO_ID), true) } returns listOf(VIDEO_ID)
        every { videoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO.copy(visible = true))

        mockMvc
            .put("$BASE_URL/$VIDEO_ID/visibility") {
                with(oauth2Login())
                with(csrf())
                param("visible", "true")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""hx-put="$BASE_URL/$VIDEO_ID/visibility?visible=false"""")) }
                content { string(containsString("“$TITLE” is shown on the website.")) }
            }
    }

    @Test
    internal fun `should say why a video in a hidden event stays hidden`() {
        every { videoService.changeVideoVisibility(listOf(VIDEO_ID), true) } throws IN_HIDDEN_EVENT
        every { videoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO.copy(visible = false))

        mockMvc
            .put("$BASE_URL/$VIDEO_ID/visibility") {
                with(oauth2Login())
                with(csrf())
                param("visible", "true")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""hx-put="$BASE_URL/$VIDEO_ID/visibility?visible=true"""")) }
                content {
                    string(containsString("""<small class="field-error" role="alert">Its event is hidden. Show the event first.</small>"""))
                }
                content { string(not(containsString("hx-swap-oob"))) }
            }
    }

    @Test
    internal fun `should not hide other database errors when showing a video`() {
        every { videoService.changeVideoVisibility(any(), any()) } throws DataIntegrityViolationException("other")

        shouldThrow<ServletException> {
            mockMvc.put("$BASE_URL/$VIDEO_ID/visibility") {
                with(oauth2Login())
                with(csrf())
                param("visible", "true")
            }
        }
        shouldThrow<ServletException> {
            mockMvc.post("$BASE_URL/visibility") {
                with(oauth2Login())
                with(csrf())
                param("videoIds", VIDEO_ID.toString())
                param("visible", "true")
            }
        }
    }

    @Test
    internal fun `should publish none of the selected videos when one is in a hidden event`() {
        every { videoService.changeVideoVisibility(listOf(VIDEO_ID), true) } throws IN_HIDDEN_EVENT

        mockMvc
            .post("$BASE_URL/visibility") {
                with(oauth2Login())
                with(csrf())
                param("videoIds", VIDEO_ID.toString())
                param("visible", "true")
            }.andExpectAll {
                status { is3xxRedirection() }
                flash { attribute("flash", Flash("video.flash.inHiddenEvent", error = true)) }
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
                param("q", "gólya tábor")
                param("page", "2")
                param("size", "50")
                param("sort", "title,desc")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL?q=g%C3%B3lya+t%C3%A1bor&page=2&size=50&sort=title%2Cdesc")
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
                redirectedUrl("$BASE_URL?q=&page=0&size=25&sort=")
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
                model { attribute("form", NewVideoForm()) }
                content { string(containsString("""id="title" required maxlength="250"""")) }
                content { string(containsString("""aria-describedby="title-help" name="title" value="">""")) }
                content { string(not(containsString("aria-invalid"))) }
                content { string(not(containsString("form-error"))) }
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
                content {
                    string(
                        containsString("""<small id="title-error" class="field-error">Another video already uses this title.</small>"""),
                    )
                }
                content { string(containsString("""id="title" required maxlength="250"""")) }
                content { string(containsString("""aria-describedby="title-help title-error" name="title"""")) }
                content { string(containsString("""aria-invalid="true"""")) }
                content { string(containsString("""<div id="form-error" role="alert"""")) }
                content { string(containsString("""<div class="cds--inline-notification__subtitle">Some fields need fixing.""")) }
            }
    }

    @Test
    internal fun `should not create a video with a blank title`() {
        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("title", "   ")
            }.andExpectAll {
                status { isOk() }
                view { name("admin/video/new") }
                content { string(containsString("""<small id="title-error" class="field-error">Fill this in.</small>""")) }
                content { string(containsString("""data-unsaved="pending">""")) }
            }

        verify(exactly = 0) { videoService.insertVideo(any()) }
    }

    @Test
    internal fun `should explain the errors in Hungarian too`() {
        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("lang", "hu")
                param("title", "x".repeat(251))
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<small id="title-error" class="field-error">Legfeljebb 250 karakter lehet.</small>""")) }
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
                content { string(containsString("""id="title" required maxlength="250"""")) }
                content { string(containsString("""name="title" value="G&oacute;lyat&aacute;bor 2026 - V&aacute;m">""")) }
                content { string(not(containsString("aria-invalid"))) }
                content { string(containsString("""data-unsaved="track">""")) }
                content { string(containsString("Image uploads are coming in a later update.")) }
                content { string(containsString("""<span class="unsaved" role="status" data-message="Unsaved changes"></span>""")) }
                content { string(containsString("""<dialog id="delete-dialog" aria-labelledby="delete-dialog-title"""")) }
                content {
                    string(
                        containsString(
                            """<h2 class="cds--modal-header__heading" id="delete-dialog-title">Delete “${DETAILED_VIDEO.title}”?</h2>""",
                        ),
                    )
                }
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
    internal fun `should explain why a video in a hidden event can't be shown`() {
        every { videoService.updateVideo(VIDEO_ID, any()) } throws IN_HIDDEN_EVENT

        mockMvc
            .post("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
                param("shootingDateStart", "2026-08-13")
                param("shootingDateEnd", "2026-08-13")
                param("visible", "true")
            }.andExpectAll {
                status { isOk() }
                view { name("admin/video/edit") }
                content { string(containsString("""id="visible" name="visible" value="true"""")) }
                content { string(containsString("""aria-describedby="visible-error"""")) }
                content {
                    string(
                        containsString(
                            """<small id="visible-error" class="field-error">This video is in a hidden event. Show the event first, then the video.</small>""",
                        ),
                    )
                }
            }
    }

    @Test
    internal fun `should not save a video that ends before it starts`() {
        mockMvc
            .post("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
                param("urls", URL)
                param("description", "d".repeat(2001))
                param("shootingDateStart", "2026-08-13")
                param("shootingDateEnd", "2026-08-12")
            }.andExpectAll {
                status { isOk() }
                view { name("admin/video/edit") }
                content {
                    string(
                        containsString("""<small id="datesInOrder-error" class="field-error">The end can’t be before the start.</small>"""),
                    )
                }
                content { string(containsString("""id="shootingDateEnd" required""")) }
                content { string(containsString("""aria-describedby="datesInOrder-error" name="shootingDateEnd"""")) }
                content {
                    string(
                        containsString("""<small id="description-error" class="field-error">Use at most 2,000 characters.</small>"""),
                    )
                }
            }

        verify(exactly = 0) { videoService.updateVideo(any(), any()) }
    }

    @Test
    internal fun `should ask for the shooting dates`() {
        mockMvc
            .post("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
                param("shootingDateStart", "")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<small id="shootingDateStart-error" class="field-error">Fill this in.</small>""")) }
                content { string(containsString("""id="shootingDateEnd" required""")) }
                content { string(containsString("""aria-describedby="shootingDateEnd-error" name="shootingDateEnd"""")) }
                content { string(not(containsString("datesInOrder-error"))) }
            }
    }

    @Test
    internal fun `should refuse a value that is not a date`() {
        mockMvc
            .post("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
                with(csrf())
                param("title", TITLE)
                param("shootingDateStart", "2026-08-13")
                param("shootingDateEnd", "soon")
            }.andExpect {
                status { isBadRequest() }
            }

        verify(exactly = 0) { videoService.updateVideo(any(), any()) }
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
                content { string(containsString("""<section id="crew" class="cds--tile panel">""")) }
                content { string(containsString("""autofocus="autofocus"""")) }
                content { string(not(containsString("<html"))) }
            }

        verify { videoCrewService.addPosition(request) }
    }

    @Test
    internal fun `should add a crew member in a new position`() {
        val request = VideoCrewRequest(VIDEO_ID, "Sminkes", MEMBER.id)
        every { videoCrewService.addPosition(request) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .post("$BASE_URL/$VIDEO_ID/crew") {
                with(oauth2Login())
                with(csrf())
                param("memberId", MEMBER.id.toString())
                param("position", "+")
                param("newPosition", " Sminkes ")
            }.andExpect {
                status { isOk() }
            }

        verify { videoCrewService.addPosition(request) }
    }

    @Test
    internal fun `should refuse a crew member without a position or with too long a one`() {
        listOf(" ", "p".repeat(251)).forEach { position ->
            mockMvc
                .post("$BASE_URL/$VIDEO_ID/crew") {
                    with(oauth2Login())
                    with(csrf())
                    param("memberId", MEMBER.id.toString())
                    param("position", "+")
                    param("newPosition", position)
                }.andExpect {
                    status { isBadRequest() }
                }
        }

        verify(exactly = 0) { videoCrewService.addPosition(any()) }
    }

    @Test
    internal fun `should list positions in Hungarian order, then the new position option`() {
        every { videoCrewService.getPositions() } returns listOf("Vágó", "Élő kapcsolás", "Operatőr")

        mockMvc
            .get("$BASE_URL/$VIDEO_ID") {
                with(oauth2Login())
            }.andExpect {
                content {
                    string(
                        stringContainsInOrder(
                            ">Élő kapcsolás</option>",
                            ">Operatőr</option>",
                            ">Vágó</option>",
                            """<option value="+" data-other>New position…</option>""",
                        ),
                    )
                }
            }
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
        private val IN_HIDDEN_EVENT =
            DataIntegrityViolationException(
                "could not execute statement",
                RuntimeException("ERROR: video_in_hidden_event: video is in a hidden event, so it can't be shown"),
            )
        private const val TITLE = "Gólyatábor 2026 - Vám"
        private const val URL = "golyatabor-2026-vam"
        private const val POSITION = "Camera"
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
