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
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultEventServiceTest {

    @MockK
    private lateinit var mockRepository: EventRepository
    @MockK
    private lateinit var mockDetailedRepository: DetailedEventRepository
    @MockK
    private lateinit var mockMapper: EventMapper
    @InjectMockKs
    private lateinit var underTest: DefaultEventService

    @Test
    internal fun `should return all events`() {
        every { mockRepository.findAll() } returns EVENT_ENTITY_LIST
        every { mockMapper.entityToModel(EVENT_ENTITY) } returns EVENT

        val response = underTest.findAllEvent()

        assertThat(response).isEqualTo(EVENT_LIST)
    }

    @Test
    internal fun `should insert new event`() {
        every { mockMapper.modelToEntity(CREATE_EVENT) } returns EVENT_ENTITY
        every { mockRepository.save(EVENT_ENTITY) } returns EVENT_ENTITY
        every { mockMapper.entityToModel(EVENT_ENTITY) } returns EVENT

        val response = underTest.insertEvent(CREATE_EVENT)

        assertThat(response).isEqualTo(EVENT)
    }

    @Test
    internal fun `should archive event`() {
        val eventIds = listOf(EVENT_ID)
        every { mockRepository.findAllById(eventIds) } returns listOf(EVENT_ENTITY)
        val updatedEventEntity = EVENT_ENTITY.copy(visible = true)
        every { mockRepository.save(updatedEventEntity) } returns updatedEventEntity
        every { mockMapper.entityToModel(updatedEventEntity) } returns EVENT

        val response = underTest.changeVisibility(eventIds)

        assertThat(response).isEqualTo(listOf(EVENT_ID))
    }

    @Test
    internal fun `should archive event with implicit archive flag`() {
        val eventIds = listOf(EVENT_ID)
        every { mockRepository.findAllById(eventIds) } returns listOf(EVENT_ENTITY)
        val updatedEventEntity = EVENT_ENTITY.copy(visible = true)
        every { mockRepository.save(updatedEventEntity) } returns updatedEventEntity
        every { mockMapper.entityToModel(updatedEventEntity) } returns EVENT

        val response = underTest.changeVisibility(eventIds, true)

        assertThat(response).isEqualTo(listOf(EVENT_ID))
    }

    @Test
    internal fun `should find event by id`() {
        every { mockDetailedRepository.findById(EVENT_ID) } returns Optional.of(DETAILED_EVENT_ENTITY)
        every { mockMapper.entityToModel(DETAILED_EVENT_ENTITY) } returns DETAILED_EVENT

        val response = underTest.findEventById(EVENT_ID)

        assertThat(response).isEqualTo(Optional.of(DETAILED_EVENT))
    }

    @Test
    internal fun `should return empty if event was not found`() {
        every { mockDetailedRepository.findById(EVENT_ID) } returns Optional.empty()

        val response = underTest.findEventById(EVENT_ID)

        assertThat(response).isEqualTo(Optional.empty<DetailedEvent>())
    }

    @Test
    internal fun `should not update event if id was not found`() {
        every { mockDetailedRepository.findById(EVENT_ID) } returns Optional.empty()

        val response = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)

        assertThat(response).isEqualTo(Optional.empty<DetailedEvent>())
    }

    @Test
    internal fun `should update event`() {
        every { mockDetailedRepository.findById(EVENT_ID) } returns Optional.of(DETAILED_EVENT_ENTITY)
        val updatedEntity = DETAILED_EVENT_ENTITY.copy(
            url = UPDATE_EVENT.url,
            title = UPDATE_EVENT.title,
            description = UPDATE_EVENT.description,
            date = UPDATE_EVENT.date,
            visible = UPDATE_EVENT.visible,
        )
        every { mockDetailedRepository.save(updatedEntity) } returns updatedEntity
        val updatedDetailedEvent = DETAILED_EVENT.copy(
            url = UPDATE_EVENT.url,
            title = UPDATE_EVENT.title,
            description = UPDATE_EVENT.description,
            date = UPDATE_EVENT.date,
            visible = UPDATE_EVENT.visible,
        )
        every { mockMapper.entityToModel(updatedEntity) } returns updatedDetailedEvent

        val response = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)

        assertThat(response).isEqualTo(Optional.of(updatedDetailedEvent))
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(EVENT_ID) } returns Unit

        underTest.removeEvent(EVENT_ID)
    }

    private companion object {
        private val EVENT_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private val EVENT = Event(EVENT_ID, "url", "name", "description", LocalDate.of(2022, 1, 1), false)
        private val EVENT_LIST = listOf(EVENT)
        private val EVENT_ENTITY = EventEntity(EVENT_ID, "url", "title")
        private val EVENT_ENTITY_LIST = listOf(EVENT_ENTITY)
        private val CREATE_EVENT = CreateEvent("url", "title")
        private val UPDATE_EVENT = UpdateEvent("updatedUrl", "updatedTitle", "updatedDescription", LocalDate.of(2022, 2, 2), true)
        private val DETAILED_EVENT = DetailedEvent(EVENT_ID, "url", "title", "description", LocalDate.of(2022, 1, 1), false, listOf())
        private val DETAILED_EVENT_ENTITY = DetailedEventEntity(EVENT_ID, "url", "title", "description", LocalDate.of(2022, 1, 1), false, listOf())
    }
}
