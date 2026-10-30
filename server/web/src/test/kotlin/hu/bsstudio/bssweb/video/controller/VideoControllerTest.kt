package hu.bsstudio.bssweb.video.controller

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import hu.bsstudio.bssweb.revision.model.RevisionType
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import tools.jackson.databind.ObjectMapper
import java.time.Instant
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(VideoController::class)
internal class VideoControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val objectMapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var mockService: VideoService

    @Test
    internal fun getAllVideos() {
        every { mockService.findAllVideos() } returns listOf(VIDEO)

        mockMvc
            .get("$BASE_URL/all") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(listOf(VIDEO)) }
            }
    }

    @Test
    internal fun getAllVideosPaged() {
        every { mockService.findAllVideos(PAGEABLE) } returns PAGED_VIDEOS

        mockMvc
            .get(BASE_URL) {
                param("page", "${PAGEABLE.pageNumber}")
                param("size", "${PAGEABLE.pageSize}")
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(PAGED_VIDEOS) }
            }
    }

    @Test
    internal fun createVideo() {
        every { mockService.insertVideo(CREATE_VIDEO) } returns VIDEO

        mockMvc
            .post(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(CREATE_VIDEO)
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isCreated() }
                content { objectMapper.writeValueAsString(VIDEO) }
                header { string("Location", "http://localhost$BASE_URL/$VIDEO_ID") }
            }
    }

    @Test
    internal fun changeVideoVisibility() {
        val videoIds = listOf(VIDEO_ID)
        val visible = false
        every { mockService.changeVideoVisibility(videoIds, visible) } returns videoIds

        mockMvc
            .put("$BASE_URL/visible") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(videoIds)
                param("visible", "$visible")
                param("videoIds", "$VIDEO_ID")
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(videoIds) }
            }
    }

    @Test
    internal fun getVideo1() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .get("$BASE_URL/$VIDEO_ID") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
            }
    }

    @Test
    internal fun getVideo2() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.empty()

        mockMvc
            .get("$BASE_URL/$VIDEO_ID") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isNotFound() }
                content { string("") }
            }
    }

    @Test
    internal fun updateVideo1() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .put("$BASE_URL/$VIDEO_ID") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(UPDATE_VIDEO)
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
            }
    }

    @Test
    internal fun updateVideo2() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.empty()

        mockMvc
            .put("$BASE_URL/$VIDEO_ID") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(UPDATE_VIDEO)
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isNotFound() }
                content { string("") }
            }
    }

    @Test
    internal fun deleteVideo() {
        every { mockService.deleteVideoById(VIDEO_ID) } returns Unit

        mockMvc
            .delete("$BASE_URL/$VIDEO_ID") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isNoContent() }
                content { string("") }
            }
    }

    @Test
    internal fun `should return the deleted videos`() {
        every { mockService.findDeletedVideos(DELETED_PAGEABLE) } returns PageImpl(listOf(VIDEO), DELETED_PAGEABLE, 1)

        mockMvc
            .get("$BASE_URL/deleted") {
                param("page", "0")
                param("size", "20")
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                jsonPath("$.content[0].id") { value("$VIDEO_ID") }
            }
    }

    @Test
    internal fun `should restore a deleted video`() {
        every { mockService.restoreVideo(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .post("$BASE_URL/$VIDEO_ID/restore") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(DETAILED_VIDEO)) }
            }
    }

    @Test
    internal fun `should answer 404 when there is no deleted video to restore`() {
        every { mockService.restoreVideo(VIDEO_ID) } returns Optional.empty()

        mockMvc
            .post("$BASE_URL/$VIDEO_ID/restore") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpect { status { isNotFound() } }
    }

    @Test
    internal fun `should answer 409 when the restored video clashes with another`() {
        every { mockService.restoreVideo(VIDEO_ID) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post("$BASE_URL/$VIDEO_ID/restore") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpect { status { isConflict() } }
    }

    @Test
    internal fun `should return the revisions of a video`() {
        every { mockService.findVideoRevisions(VIDEO_ID) } returns listOf(REVISION_INFO)

        mockMvc
            .get("$BASE_URL/$VIDEO_ID/revisions") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(listOf(REVISION_INFO))) }
            }
    }

    @Test
    internal fun `should return a video as it was at a revision`() {
        every { mockService.findVideoRevision(VIDEO_ID, REVISION) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .get("$BASE_URL/$VIDEO_ID/revisions/$REVISION") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(DETAILED_VIDEO)) }
            }
    }

    @Test
    internal fun `should restore a revision of a video`() {
        every { mockService.restoreVideoRevision(VIDEO_ID, REVISION) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .post("$BASE_URL/$VIDEO_ID/revisions/$REVISION/restore") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(DETAILED_VIDEO)) }
            }
    }

    private companion object {
        private const val REVISION = 3L
        private val REVISION_INFO = RevisionInfo(REVISION, Instant.parse("2026-10-30T12:00:00Z"), "author", RevisionType.UPDATED)
        private val DELETED_PAGEABLE: Pageable = PageRequest.of(0, 20)
        private const val BASE_URL = "/api/v1/video"
        private const val USERNAME = "user"
        private const val PASSWORD = "password"
        private val PAGEABLE: Pageable = PageRequest.of(0, 20)
        private val VIDEO_ID = UUID.randomUUID()
        private val URLS = listOf("url")
        private const val TITLE = "title"
        private val SHOOTING_DATE_START = LocalDate.EPOCH
        private val SHOOTING_DATE_END = LocalDate.MAX
        private const val VISIBLE = false
        private const val DESCRIPTION = "description"
        private val VIDEO =
            Video(
                id = VIDEO_ID,
                title = TITLE,
                urls = URLS,
                description = DESCRIPTION,
                shootingDateStart = SHOOTING_DATE_START,
                shootingDateEnd = SHOOTING_DATE_END,
                visible = VISIBLE,
            )
        private val PAGED_VIDEOS = PageImpl(listOf(VIDEO), PAGEABLE, 1)
        private val CREATE_VIDEO = CreateVideo(title = TITLE)
        private val DETAILED_VIDEO =
            DetailedVideo(
                id = VIDEO_ID,
                title = TITLE,
                urls = URLS,
                description = DESCRIPTION,
                shootingDateStart = SHOOTING_DATE_START,
                shootingDateEnd = SHOOTING_DATE_END,
                visible = VISIBLE,
                labels = listOf(),
                crew = listOf(),
            )
        private val UPDATE_VIDEO =
            UpdateVideo(
                title = TITLE,
                urls = URLS,
                description = DESCRIPTION,
                shootingDateStart = SHOOTING_DATE_START,
                shootingDateEnd = SHOOTING_DATE_END,
                visible = VISIBLE,
                labels = listOf(),
            )
    }
}
