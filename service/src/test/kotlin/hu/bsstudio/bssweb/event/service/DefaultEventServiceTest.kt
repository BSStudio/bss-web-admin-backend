package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.mapper.EventMapper
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.repository.DetailedEventRepository
import hu.bsstudio.bssweb.event.repository.EventRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

internal class DefaultEventServiceTest {

    @MockK
    private lateinit var mockRepository: EventRepository

    @MockK
    private lateinit var mockDetailedRepository: DetailedEventRepository

    @MockK
    private lateinit var mockMapper: EventMapper

    private lateinit var underTest: DefaultEventService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultEventService(mockRepository, mockDetailedRepository, mockMapper)
    }

    @Test
    internal fun `should return all events`() {
        every { mockRepository.findAll() } returns eventEntityList
        every { mockMapper.entityToModel(eventEntity1) } returns event1

        val response = underTest.findAllEvent()

        assertThat(response).isEqualTo(eventList)
    }

    @Test
    internal fun `should insert new event`() {
        every { mockMapper.modelToEntity(createEvent) } returns eventEntity1
        every { mockRepository.save(eventEntity1) } returns eventEntity1
        every { mockMapper.entityToModel(eventEntity1) } returns event1

        val response = underTest.insertEvent(createEvent)

        assertThat(response).isEqualTo(event1)
    }

    @Test
    internal fun `should archive event`() {
        val eventIds = listOf(eventId1)
        every { mockRepository.findAllById(eventIds) } returns listOf(eventEntity1)
        val updatedEventEntity = eventEntity1.copy(visible = true)
        every { mockRepository.save(updatedEventEntity) } returns updatedEventEntity
        every { mockMapper.entityToModel(updatedEventEntity) } returns event1

        val response = underTest.changeVisibility(eventIds)

        assertThat(response).isEqualTo(listOf(eventId1))
    }

    @Test
    internal fun `should archive event with implicit archive flag`() {
        val eventIds = listOf(eventId1)
        every { mockRepository.findAllById(eventIds) } returns listOf(eventEntity1)
        val updatedEventEntity = eventEntity1.copy(visible = true)
        every { mockRepository.save(updatedEventEntity) } returns updatedEventEntity
        every { mockMapper.entityToModel(updatedEventEntity) } returns event1

        val response = underTest.changeVisibility(eventIds, true)

        assertThat(response).isEqualTo(listOf(eventId1))
    }

    @Test
    internal fun `should find event by id`() {
        every { mockDetailedRepository.findById(eventId1) } returns Optional.of(detailedEventEntity)
        every { mockMapper.entityToModel(detailedEventEntity) } returns detailedEvent

        val response = underTest.findEventById(eventId1)

        assertThat(response).isEqualTo(Optional.of(detailedEvent))
    }

    @Test
    internal fun `should return empty if event was not found`() {
        every { mockDetailedRepository.findById(eventId1) } returns Optional.empty()

        val response = underTest.findEventById(eventId1)

        assertThat(response).isEqualTo(Optional.empty<DetailedEvent>())
    }

    @Test
    internal fun `should not update event if id was not found`() {
        every { mockDetailedRepository.findById(eventId1) } returns Optional.empty()

        val response = underTest.updateEvent(eventId1, updateEvent)

        assertThat(response).isEqualTo(Optional.empty<DetailedEvent>())
    }

    @Test
    internal fun `should update event`() {
        every { mockDetailedRepository.findById(eventId1) } returns Optional.of(detailedEventEntity)
        val updatedEntity = detailedEventEntity.copy(
            url = updateEvent.url,
            title = updateEvent.title,
            description = updateEvent.description,
            date = updateEvent.date,
            visible = updateEvent.visible,
        )
        every { mockDetailedRepository.save(updatedEntity) } returns updatedEntity
        val updatedDetailedEvent = detailedEvent.copy(
            url = updateEvent.url,
            title = updateEvent.title,
            description = updateEvent.description,
            date = updateEvent.date,
            visible = updateEvent.visible,
        )
        every { mockMapper.entityToModel(updatedEntity) } returns updatedDetailedEvent

        val response = underTest.updateEvent(eventId1, updateEvent)

        assertThat(response).isEqualTo(Optional.of(updatedDetailedEvent))
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(eventId1) } returns Unit

        underTest.removeEvent(eventId1)
    }

    private companion object {
        private val eventId1 = UUID.fromString("01234567-0123-0123-0123-0123456789AB")
        private val event1 = Event(eventId1, "url", "name", "description", LocalDate.of(2022, 1, 1), false)
        private val eventList = listOf(event1)
        private val eventEntity1 = EventEntity(eventId1, "url", "title")
        private val eventEntityList = listOf(eventEntity1)
        private val createEvent = CreateEvent("url", "title")
        private val updateEvent = UpdateEvent("updatedUrl", "updatedTitle", "updatedDescription", LocalDate.of(2022, 2, 2), true)
        private val detailedEvent = DetailedEvent(eventId1, "url", "title", "description", LocalDate.of(2022, 1, 1), false, listOf())
        private val detailedEventEntity = DetailedEventEntity(eventId1, "url", "title", "description", LocalDate.of(2022, 1, 1), false, listOf())
    }
}
