package hu.bsstudio.bssweb.event.controller

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.service.EventService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class EventControllerTest {

    @MockK
    private lateinit var mockService: EventService

    @InjectMockKs
    private lateinit var underTest: EventController

    @Test
    internal fun findAllEvent() {
        every { mockService.findAllEvent() } returns EVENT_LIST

        val response = underTest.findAllEvent()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(EVENT_LIST)
    }

    @Test
    @Disabled
    internal fun createEvent() {
        every { mockService.insertEvent(CREATE_EVENT) } returns EVENT
        every { EVENT.id } returns EVENT_ID

        val response = underTest.createEvent(CREATE_EVENT)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(EVENT)
    }

    @Test
    internal fun findEventById1() {
        every { mockService.findEventById(EVENT_ID) } returns Optional.of(DETAILED_EVENT)

        val response = underTest.findEventById(EVENT_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_EVENT)
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
        every { mockService.updateEvent(EVENT_ID, UPDATE_EVENT) } returns Optional.of(DETAILED_EVENT)

        val response = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_EVENT)
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
        private val EVENT_ID = mockk<UUID>()
        private val EVENT = mockk<Event>()
        private val CREATE_EVENT = mockk<CreateEvent>()
        private val DETAILED_EVENT = mockk<DetailedEvent>()
        private val UPDATE_EVENT = mockk<UpdateEvent>()
        private val EVENT_LIST = mockk<List<Event>>()
    }
}
