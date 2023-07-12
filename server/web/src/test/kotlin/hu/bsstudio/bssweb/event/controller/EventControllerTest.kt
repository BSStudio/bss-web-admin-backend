package hu.bsstudio.bssweb.event.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.WebConfiguration
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.security.config.SecurityConfig
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID

@WebMvcTest(EventController::class)
@Import(SecurityConfig::class, EventController::class)
@ContextConfiguration(classes = [WebConfiguration::class])
internal class EventControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var mockService: EventService

    @Test
    internal fun findAllEvent() {
        every { mockService.findAllEvent() } returns EVENT_LIST

        mockMvc.get("/api/event") {
            user("user")
            httpBasic("user", "password")
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(objectMapper.writeValueAsString(EVENT_LIST)) }
        }
    }

//    @Test
//    internal fun createEvent() {
//        every { mockService.insertEvent(CREATE_EVENT) } returns EVENT
//        every { EVENT.id } returns EVENT_ID
//
//        val response = underTest.createEvent(CREATE_EVENT)
//
//        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
//        assertThat(response.body).isEqualTo(EVENT)
//    }
//
//    @Test
//    internal fun findEventById1() {
//        every { mockService.findEventById(EVENT_ID) } returns Optional.of(DETAILED_EVENT)
//
//        val response = underTest.findEventById(EVENT_ID)
//
//        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
//        assertThat(response.body).isEqualTo(DETAILED_EVENT)
//    }
//
//    @Test
//    internal fun findEventById2() {
//        every { mockService.findEventById(EVENT_ID) } returns Optional.empty()
//
//        val response = underTest.findEventById(EVENT_ID)
//
//        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
//        assertThat(response.body).isEqualTo(null)
//    }
//
//    @Test
//    internal fun updateEvent1() {
//        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.of(DETAILED_EVENT)
//
//        val response = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)
//
//        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
//        assertThat(response.body).isEqualTo(DETAILED_EVENT)
//    }
//
//    @Test
//    internal fun updateEvent2() {
//        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.empty()
//
//        val response = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)
//
//        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
//        assertThat(response.body).isEqualTo(null)
//    }
//
//    @Test
//    internal fun deleteEvent() {
//        every { mockService.removeEvent(EVENT_ID) } returns Unit
//
//        underTest.deleteEvent(EVENT_ID)
//    }

    private companion object {
        private val EVENT_ID = mockk<UUID>()
        private val EVENT = mockk<Event>()
        private val CREATE_EVENT = mockk<CreateEvent>()
        private val DETAILED_EVENT = mockk<DetailedEvent>()
        private val UPDATE_EVENT = mockk<UpdateEvent>()
        private val EVENT_LIST = mockk<List<Event>>()
    }
}
