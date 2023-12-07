package hu.bsstudio.bssweb.video.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(VideoController::class)
internal class VideoControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var mockService: VideoService

    @Test
    internal fun getAllVideos() {
        every { mockService.findAllVideos() } returns listOf(VIDEO)

        mockMvc.get("$BASE_URL/all") {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(listOf(VIDEO)) }
        }
    }

    @Test
    internal fun getAllVideosPaged() {
        every { mockService.findAllVideos(PAGEABLE) } returns PAGED_VIDEOS

        mockMvc.get(BASE_URL) {
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

        mockMvc.post(BASE_URL) {
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

        mockMvc.put("$BASE_URL/visible") {
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

        mockMvc.get("$BASE_URL/$VIDEO_ID") {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
        }
    }

    @Test
    internal fun getVideo2() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.empty()

        mockMvc.get("$BASE_URL/$VIDEO_ID") {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpectAll {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun updateVideo1() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.of(DETAILED_VIDEO)

        mockMvc.put("$BASE_URL/$VIDEO_ID") {
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

        mockMvc.put("$BASE_URL/$VIDEO_ID") {
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

        mockMvc.delete("$BASE_URL/$VIDEO_ID") {
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpectAll {
            status { isNoContent() }
            content { string("") }
        }
    }

    private companion object {
        private const val BASE_URL = "/api/v1/video"
        private const val USERNAME = "user"
        private const val PASSWORD = "password"
        private val PAGEABLE: Pageable = PageRequest.of(0, 20)
        private val VIDEO_ID = UUID.randomUUID()
        private const val URL = "url"
        private const val TITLE = "title"
        private val UPLOADED_AT = LocalDate.now()
        private const val VISIBLE = false
        private const val DESCRIPTION = "description"
        private val VIDEO = Video(id = VIDEO_ID, url = URL, title = TITLE, uploadedAt = UPLOADED_AT, visible = VISIBLE)
        private val PAGED_VIDEOS = PageImpl(listOf(VIDEO))
        private val CREATE_VIDEO = CreateVideo(url = URL, title = TITLE)
        private val DETAILED_VIDEO =
            DetailedVideo(
                id = VIDEO_ID,
                url = URL,
                title = TITLE,
                description = DESCRIPTION,
                uploadedAt = UPLOADED_AT,
                visible = VISIBLE,
                crew = listOf(),
            )
        private val UPDATE_VIDEO =
            UpdateVideo(url = URL, title = TITLE, description = DESCRIPTION, uploadedAt = UPLOADED_AT, visible = VISIBLE)
    }
}
