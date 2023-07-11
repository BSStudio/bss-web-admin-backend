package hu.bsstudio.bssweb.videocrew.controller

import com.fasterxml.jackson.databind.ObjectMapper
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(VideoCrewController::class)
@ContextConfiguration(classes = [VideoCrewControllerTest::class])
internal class VideoCrewControllerTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockK
    private lateinit var mockService: VideoCrewService

    @Test
    internal fun getPositions() {
        every { mockService.getPositions() } returns listOf(POSITION)

        mockMvc.get("/api/v1/videocrew").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(listOf(POSITION)) }
        }
    }

    @Test
    internal fun addPosition() {
        every { mockService.addPosition(VIDEO_CREW_REQUEST) } returns Optional.of(DETAILED_VIDEO)

        mockMvc.post("/api/v1/videocrew") {
            content = objectMapper.writeValueAsString(VIDEO_CREW_REQUEST)
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
        }
    }

    @Test
    internal fun addPositionEmpty() {
        every { mockService.addPosition(VIDEO_CREW_REQUEST) } returns Optional.empty()

        mockMvc.post("/api/v1/videocrew") {
            content = objectMapper.writeValueAsString(VIDEO_CREW_REQUEST)
        }.andExpectAll {
            status { HttpStatus.NOT_FOUND }
            content { string("") }
        }
    }

    @Test
    internal fun removePosition() {
        every { mockService.removePosition(VIDEO_CREW_REQUEST) } returns Optional.of(DETAILED_VIDEO)

        mockMvc.post("/api/v1/videocrew/remove") {
            content = objectMapper.writeValueAsString(VIDEO_CREW_REQUEST)
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
        }
    }

    @Test
    internal fun removePositionEmpty() {
        every { mockService.removePosition(VIDEO_CREW_REQUEST) } returns Optional.empty()

        mockMvc.post("/api/v1/videocrew/remove") {
            content = objectMapper.writeValueAsString(VIDEO_CREW_REQUEST)
        }.andExpectAll {
            status { HttpStatus.NOT_FOUND }
            content { string("") }
        }
    }

    private companion object {
        private val VIDEO_ID = UUID.randomUUID()
        private const val POSITION = "position"
        private val MEMBER_ID = UUID.randomUUID()
        private val VIDEO_CREW_REQUEST = VideoCrewRequest(VIDEO_ID, POSITION, MEMBER_ID)
        private val DETAILED_VIDEO = DetailedVideo(id = VIDEO_ID, url = "url", title = "title", description = "description", uploadedAt = LocalDate.now(), visible = false, crew = listOf())
    }
}
