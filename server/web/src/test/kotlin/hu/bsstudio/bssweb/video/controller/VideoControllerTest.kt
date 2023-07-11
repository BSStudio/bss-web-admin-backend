package hu.bsstudio.bssweb.video.controller

import com.fasterxml.jackson.databind.ObjectMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(VideoController::class)
@ContextConfiguration(classes = [VideoControllerTest::class])
internal class VideoControllerTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockK
    private lateinit var mockService: VideoService

    @Test
    internal fun getAllVideos() {
        every { mockService.findAllVideos() } returns listOf(VIDEO)

        mockMvc.get("/api/v1/videos").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(listOf(VIDEO)) }
        }
    }

    @Test
    internal fun getAllVideosPaged() {
        every { mockService.findAllVideos(PAGEABLE) } returns PAGED_VIDEOS

        mockMvc.get("/api/v1/videos?page=${PAGEABLE.pageNumber}&size=${PAGEABLE.pageSize}").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(PAGED_VIDEOS) }
        }
    }

    @Test
    internal fun createVideo() {
        every { mockService.insertVideo(CREATE_VIDEO) } returns VIDEO

        mockMvc.post("/api/v1/videos") {
            content = objectMapper.writeValueAsString(CREATE_VIDEO)
        }.andExpectAll {
            status { isCreated() }
            content { objectMapper.writeValueAsString(VIDEO) }
        }
    }

    @Test
    internal fun changeVideoVisibility() {
        val videoIds = listOf(VIDEO_ID)
        val visible = false
        every { mockService.changeVideoVisibility(videoIds, visible) } returns videoIds

        mockMvc.post("/api/v1/videos/visibility") {
            content = objectMapper.writeValueAsString(videoIds)
            param("visible", visible.toString())
            param("videoIds", videoIds.joinToString(","))
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(videoIds) }
        }
    }

    @Test
    internal fun getVideo1() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        mockMvc.get("/api/v1/videos/$VIDEO_ID").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
        }
    }

    @Test
    internal fun getVideo2() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.empty()

        mockMvc.get("/api/v1/videos/$VIDEO_ID").andExpectAll {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun updateVideo1() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.of(DETAILED_VIDEO)

        mockMvc.post("/api/v1/videos/$VIDEO_ID") {
            content = objectMapper.writeValueAsString(UPDATE_VIDEO)
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
        }
    }

    @Test
    internal fun updateVideo2() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.empty()

        mockMvc.post("/api/v1/videos/$VIDEO_ID") {
            content = objectMapper.writeValueAsString(UPDATE_VIDEO)
        }.andExpectAll {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun deleteVideo() {
        every { mockService.deleteVideoById(VIDEO_ID) } returns Unit

        mockMvc.delete("/api/v1/videos/$VIDEO_ID").andExpectAll {
            status { isOk() }
            content { string("") }
        }
    }

    private companion object {
        private val PAGEABLE: Pageable = PageRequest.of(2, 10)
        private val VIDEO_ID = UUID.randomUUID()
        private const val URL = "url"
        private const val TITLE = "title"
        private val UPLOADED_AT = LocalDate.now()
        private const val VISIBLE = false
        private const val DESCRIPTION = "description"
        private val VIDEO = Video(id = VIDEO_ID, url = URL, title = TITLE, uploadedAt = UPLOADED_AT, visible = VISIBLE)
        private val PAGED_VIDEOS = PageImpl(listOf(VIDEO))
        private val CREATE_VIDEO = CreateVideo(url = URL, title = TITLE)
        private val DETAILED_VIDEO = DetailedVideo(id = VIDEO_ID, url = URL, title = TITLE, description = DESCRIPTION, uploadedAt = UPLOADED_AT, visible = VISIBLE, crew = listOf())
        private val UPDATE_VIDEO = UpdateVideo(url = URL, title = TITLE, description = DESCRIPTION, uploadedAt = UPLOADED_AT, visible = VISIBLE)
    }
}
