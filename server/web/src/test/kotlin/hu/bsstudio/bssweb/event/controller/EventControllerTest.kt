package hu.bsstudio.bssweb.event.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.video.model.Video
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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

@WebMvcTest(EventController::class)
internal class EventControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var mockService: EventService

    @Test
    internal fun findAllEvent() {
        every { mockService.findAllEvent() } returns EVENT_LIST

        mockMvc.get(BASE_URL) {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpect {
            status { isOk() }
            content { json(objectMapper.writeValueAsString(EVENT_LIST)) }
        }
    }

    @Test
    internal fun createEvent() {
        every { mockService.insertEvent(CREATE_EVENT) } returns EVENT

        mockMvc.post(BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(CREATE_EVENT)
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpect {
            status { isCreated() }
            content { json(objectMapper.writeValueAsString(EVENT)) }
            header { string("Location", "http://localhost$BASE_URL/$EVENT_ID") }
        }
    }

    @Test
    internal fun findEventById1() {
        every { mockService.findEventById(EVENT_ID) } returns Optional.of(DETAILED_EVENT)

        mockMvc.get("$BASE_URL/$EVENT_ID") {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpect {
            status { isOk() }
            content { json(objectMapper.writeValueAsString(DETAILED_EVENT)) }
        }
    }

    @Test
    internal fun findEventById2() {
        every { mockService.findEventById(EVENT_ID) } returns Optional.empty()

        mockMvc.get("$BASE_URL/$EVENT_ID") {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpect {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun updateEvent1() {
        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.of(DETAILED_EVENT)

        mockMvc.put("$BASE_URL/$EVENT_ID") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(UPDATE_EVENT)
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpect {
            status { isOk() }
            content { json(objectMapper.writeValueAsString(DETAILED_EVENT)) }
        }
    }

    @Test
    internal fun updateEvent2() {
        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.empty()

        mockMvc.put("$BASE_URL/$EVENT_ID") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(UPDATE_EVENT)
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpect {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun deleteEvent() {
        every { mockService.removeEvent(EVENT_ID) } returns Unit

        mockMvc.delete("$BASE_URL/$EVENT_ID") {
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpect {
            status { isNoContent() }
            content { string("") }
        }
    }

    private companion object {
        private const val BASE_URL = "/api/v1/event"
        private const val USERNAME = "user"
        private const val PASSWORD = "password"
        private const val URL = "url"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private val DATE_FROM = LocalDate.EPOCH
        private val DATE_TO = LocalDate.MAX
        private const val VISIBLE = true
        private val EVENT_ID = UUID.randomUUID()
        private val VIDEOS = listOf<Video>()
        private val EVENT =
            Event(
                id = EVENT_ID,
                url = URL,
                title = TITLE,
                description = DESCRIPTION,
                dateFrom = DATE_FROM,
                dateTo = DATE_TO,
                visible = VISIBLE,
            )
        private val CREATE_EVENT = CreateEvent(url = URL, title = TITLE)
        private val DETAILED_EVENT =
            DetailedEvent(
                id = EVENT_ID,
                url = URL,
                title = TITLE,
                description = DESCRIPTION,
                dateFrom = DATE_FROM,
                dateTo = DATE_TO,
                visible = VISIBLE,
                videos = VIDEOS,
            )
        private val UPDATE_EVENT =
            UpdateEvent(url = URL, title = TITLE, description = DESCRIPTION, dateFrom = DATE_FROM, dateTo = DATE_TO, visible = VISIBLE)
        private val EVENT_LIST = listOf(EVENT)
    }
}
