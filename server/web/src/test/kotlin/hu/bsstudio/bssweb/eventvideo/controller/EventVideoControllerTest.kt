package hu.bsstudio.bssweb.eventvideo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(EventVideoController::class)
internal class EventVideoControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var mockService: EventVideoService

    @Test
    fun addVideoToEvent() {
        every { mockService.addVideoToEvent(EVENT_ID, VIDEO_ID) } returns Optional.of(DETAILED_EVENT)

        mockMvc.post(BASE_URL) {
            param("eventId", EVENT_ID.toString())
            param("videoId", VIDEO_ID.toString())
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_EVENT) }
        }
    }

    @Test
    fun removeVideoFromEvent() {
        every { mockService.removeVideoFromEvent(EVENT_ID, VIDEO_ID) } returns Optional.of(DETAILED_EVENT)

        mockMvc.delete(BASE_URL) {
            param("eventId", EVENT_ID.toString())
            param("videoId", VIDEO_ID.toString())
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_EVENT) }
        }
    }

    private companion object {
        private const val BASE_URL = "/api/v1/eventVideo"
        private const val USERNAME = "user"
        private const val PASSWORD = "password"
        private val EVENT_ID = UUID.randomUUID()
        private val VIDEO_ID = UUID.randomUUID()
        private val DETAILED_EVENT =
            DetailedEvent(
                id = EVENT_ID,
                url = "url",
                title = "title",
                description = "description",
                dateFrom = LocalDate.now(),
                visible = false,
                videos = listOf(),
            )
    }
}
