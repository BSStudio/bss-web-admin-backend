package hu.bsstudio.bssweb.videocrew.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(VideoCrewController::class)
internal class VideoCrewControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var mockService: VideoCrewService

    @Test
    internal fun getPositions() {
        every { mockService.getPositions() } returns listOf(POSITION)

        mockMvc
            .get("$BASE_URL/position") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(listOf(POSITION)) }
            }
    }

    @Test
    internal fun addPosition() {
        every { mockService.addPosition(VIDEO_CREW_REQUEST) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .put(BASE_URL) {
                param("videoId", "${VIDEO_CREW_REQUEST.videoId}")
                param("memberId", "${VIDEO_CREW_REQUEST.memberId}")
                param("position", VIDEO_CREW_REQUEST.position)
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
            }
    }

    @Test
    internal fun addPositionEmpty() {
        every { mockService.addPosition(VIDEO_CREW_REQUEST) } returns Optional.empty()

        mockMvc
            .put(BASE_URL) {
                param("videoId", "${VIDEO_CREW_REQUEST.videoId}")
                param("memberId", "${VIDEO_CREW_REQUEST.memberId}")
                param("position", VIDEO_CREW_REQUEST.position)
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isNotFound() }
                content { string("") }
            }
    }

    @Test
    internal fun removePosition() {
        every { mockService.removePosition(VIDEO_CREW_REQUEST) } returns Optional.of(DETAILED_VIDEO)

        mockMvc
            .delete(BASE_URL) {
                param("videoId", "${VIDEO_CREW_REQUEST.videoId}")
                param("memberId", "${VIDEO_CREW_REQUEST.memberId}")
                param("position", VIDEO_CREW_REQUEST.position)
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(DETAILED_VIDEO) }
            }
    }

    @Test
    internal fun removePositionEmpty() {
        every { mockService.removePosition(VIDEO_CREW_REQUEST) } returns Optional.empty()

        mockMvc
            .delete(BASE_URL) {
                param("videoId", "${VIDEO_CREW_REQUEST.videoId}")
                param("memberId", "${VIDEO_CREW_REQUEST.memberId}")
                param("position", VIDEO_CREW_REQUEST.position)
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isNotFound() }
                content { string("") }
            }
    }

    private companion object {
        private const val BASE_URL = "/api/v1/videoCrew"
        private const val USERNAME = "user"
        private const val PASSWORD = "password"
        private val VIDEO_ID = UUID.randomUUID()
        private const val POSITION = "position"
        private val MEMBER_ID = UUID.randomUUID()
        private val VIDEO_CREW_REQUEST = VideoCrewRequest(VIDEO_ID, POSITION, MEMBER_ID)
        private val DETAILED_VIDEO =
            DetailedVideo(
                id = VIDEO_ID,
                urls = listOf("url"),
                title = "title",
                description = "description",
                shootingDateStart = LocalDate.now(),
                shootingDateEnd = LocalDate.now(),
                visible = false,
                crew = listOf(),
                labels = listOf(),
            )
    }
}
