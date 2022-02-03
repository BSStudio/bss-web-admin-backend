package hu.bsstudio.bssweb.event.controller

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.service.EventService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

internal class EventControllerTest {

    @MockK
    private lateinit var mockService: EventService

    private lateinit var underTest: EventController

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        this.underTest = EventController(mockService)
    }

    @Test
    internal fun findAllEvent() {
        every { mockService.findAllEvent() } returns EVENT_LIST

        val response = underTest.findAllEvent()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(EVENT_LIST)
    }

    @Test
    internal fun createEvent() {
        every { mockService.insertEvent(CREATE_EVENT) } returns EVENT_1

        val response = underTest.createEvent(CREATE_EVENT)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(EVENT_1)
    }

    @Test
    internal fun findEventById1() {
        every { mockService.findEventById(EVENT_ID) } returns Optional.of(DETAILED_EVENT_1)

        val response = underTest.findEventById(EVENT_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_EVENT_1)
    }

    @Test
    internal fun findEventById2() {
        every { mockService.findEventById(EVENT_ID) } returns Optional.empty()

        val response = underTest.findEventById(EVENT_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo(null)
    }

    @Test
    internal fun updateEvent1() {
        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.of(DETAILED_EVENT_1)

        val response = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_EVENT_1)
    }

    @Test
    internal fun updateEvent2() {
        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.empty()

        val response = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo(null)
    }

    @Test
    internal fun deleteEvent() {
        every { mockService.removeEvent(EVENT_ID) } returns Unit

        underTest.deleteEvent(EVENT_ID)
    }

    private companion object {
        private val EVENT_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private val EVENT_1 = Event(id = EVENT_ID, url = "url", title = "title", description = "description", date = LocalDate.of(2022, 1, 1), visible = false)
        private val CREATE_EVENT = CreateEvent(url = "url", title = "title")
        private val DETAILED_EVENT_1 = DetailedEvent(id = EVENT_ID, url = "url", title = "title", description = "description", date = LocalDate.of(2022, 1, 1), visible = false, videos = listOf())
        private val UPDATE_EVENT = UpdateEvent(url = "url", title = "title", description = "description", date = LocalDate.of(2022, 1, 1), visible = false)
        private val EVENT_LIST = listOf(EVENT_1)
    }
}
