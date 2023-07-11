package hu.bsstudio.bssweb.eventvideo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(EventVideoController::class)
@ContextConfiguration(classes = [EventVideoControllerTest::class])
internal class EventVideoControllerTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var mockService: EventVideoService

    @Test
    fun addVideoToEvent() {
        every { mockService.addVideoToEvent(EVENT_ID, VIDEO_ID) } returns Optional.of(DETAILED_EVENT)

        mockMvc.put("/api/v1/event/$EVENT_ID/video/$VIDEO_ID").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_EVENT) }
        }
    }

    @Test
    fun removeVideoFromEvent() {
        every { mockService.removeVideoFromEvent(EVENT_ID, VIDEO_ID) } returns Optional.of(DETAILED_EVENT)

        mockMvc.put("/api/v1/event/$EVENT_ID/video/$VIDEO_ID").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_EVENT) }
        }
    }

    private companion object {
        private val EVENT_ID = UUID.randomUUID()
        private val VIDEO_ID = UUID.randomUUID()
        private val DETAILED_EVENT = DetailedEvent(
            id = EVENT_ID,
            url = "url",
            title = "title",
            description = "description",
            date = LocalDate.now(),
            visible = false,
            videos = listOf()
        )
    }
}
