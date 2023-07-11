package hu.bsstudio.bssweb.event.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.eventvideo.controller.EventVideoController
import hu.bsstudio.bssweb.eventvideo.controller.EventVideoControllerTest
import hu.bsstudio.bssweb.security.config.SecurityConfig
import hu.bsstudio.bssweb.video.model.Video
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(EventVideoController::class)
@ContextConfiguration(classes = [EventVideoControllerTest::class])
internal class EventControllerTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var mockService: EventService

    @Test
    internal fun findAllEvent() {
        every { mockService.findAllEvent() } returns EVENT_LIST

        mockMvc.get("/api/v1/event")
                .andExpect { status { isOk() } }
                .andExpect { content { objectMapper.writeValueAsString(EVENT_LIST) } }
    }

    @Test
    internal fun createEvent() {
        every { mockService.insertEvent(CREATE_EVENT) } returns EVENT

        mockMvc.post("/api/v1/event") {
            content = objectMapper.writeValueAsString(CREATE_EVENT)
            // basic auth for username and password
            header(HttpHeaders.AUTHORIZATION, "Basic dXNlcm5hbWU6cGFzc3dvcmQ=")
        }.andExpectAll {
            status {isCreated() }
            content { objectMapper.writeValueAsString(EVENT) }
        }
    }

    @Test
    internal fun findEventById1() {
        every { mockService.findEventById(EVENT_ID) } returns Optional.of(DETAILED_EVENT)

        mockMvc.get("/api/v1/event/$EVENT_ID").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_EVENT) }
        }
    }

    @Test
    internal fun findEventById2() {
        every { mockService.findEventById(EVENT_ID) } returns Optional.empty()

        mockMvc.get("/api/v1/event/$EVENT_ID").andExpectAll {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun updateEvent1() {
        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.of(DETAILED_EVENT)

        mockMvc.put("/api/v1/event/$EVENT_ID") {
            content = objectMapper.writeValueAsString(UPDATE_EVENT)
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(DETAILED_EVENT) }
        }
    }

    @Test
    internal fun updateEvent2() {
        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.empty()

        mockMvc.put("/api/v1/event/$EVENT_ID") {
            content = objectMapper.writeValueAsString(UPDATE_EVENT)
        }.andExpectAll {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun deleteEvent() {
        every { mockService.removeEvent(EVENT_ID) } returns Unit

        mockMvc.delete("/api/v1/event/$EVENT_ID").andExpectAll {
            status { isNoContent() }
            content { string("") }
        }
    }

    private companion object {
        private const val URL = "url"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private val DATE = LocalDate.now()
        private const val VISIBLE = true
        private val EVENT_ID = UUID.randomUUID()
        private val VIDEOS = listOf<Video>()
        private val EVENT = Event(id = EVENT_ID, url = URL, title = TITLE, description = DESCRIPTION, date = DATE, visible = VISIBLE)
        private val CREATE_EVENT = CreateEvent(url = URL, title = TITLE)
        private val DETAILED_EVENT = DetailedEvent(id = EVENT_ID, url = URL, title = TITLE, description = DESCRIPTION, date = DATE, visible = VISIBLE, videos = VIDEOS)
        private val UPDATE_EVENT = UpdateEvent(url = URL, title = TITLE, description = DESCRIPTION, date = DATE, visible = VISIBLE)
        private val EVENT_LIST = listOf(EVENT)
    }
}
