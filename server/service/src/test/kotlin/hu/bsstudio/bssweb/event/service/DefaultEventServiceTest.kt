package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.mapper.EventMapper
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.revision.mapper.RevisionMapper
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.history.Revision
import org.springframework.data.history.RevisionMetadata
import org.springframework.data.history.Revisions
import java.time.Instant
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultEventServiceTest(
    @param:MockK private val mockRepository: EventRepository,
    @param:MockK private val mockMapper: EventMapper,
    @param:MockK private val mockRevisionMapper: RevisionMapper,
) {
    @InjectMockKs
    private lateinit var underTest: DefaultEventService

    @Test
    internal fun `should change the visibility of events`() {
        val eventIds = listOf(EVENT_ID)
        every { mockRepository.findAllById(eventIds) } returns listOf(EVENT_ENTITY)
        every { EVENT_ENTITY.visible = true } returns Unit
        every { mockRepository.save(EVENT_ENTITY) } returns EVENT_ENTITY
        every { EVENT_ENTITY.id } returns EVENT_ID

        val actual = underTest.changeEventVisibility(eventIds, true)

        actual.shouldContainExactly(EVENT_ID)
        verify { EVENT_ENTITY.visible = true }
    }

    @Test
    internal fun `should return all events`() {
        every { mockRepository.findAll() } returns listOf(EVENT_ENTITY)
        every { mockMapper.entityToModel(EVENT_ENTITY) } returns EVENT

        val actual = underTest.findAllEvent()

        actual.shouldContainExactly(EVENT)
    }

    @Test
    internal fun `should insert new event`() {
        every { mockMapper.modelToEntity(CREATE_EVENT) } returns EVENT_ENTITY
        every { mockRepository.save(EVENT_ENTITY) } returns EVENT_ENTITY
        every { mockMapper.entityToModel(EVENT_ENTITY) } returns EVENT

        val actual = underTest.insertEvent(CREATE_EVENT)

        actual shouldBeEqual EVENT
    }

    @Test
    internal fun `should find event by id`() {
        every { mockRepository.findById(EVENT_ID) } returns Optional.of(DETAILED_EVENT_ENTITY)
        every { mockMapper.entityToDetailedModel(DETAILED_EVENT_ENTITY) } returns DETAILED_EVENT

        val actual = underTest.findEventById(EVENT_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_EVENT }
    }

    @Test
    internal fun `should return empty if event was not found`() {
        every { mockRepository.findById(EVENT_ID) } returns Optional.empty()

        val actual = underTest.findEventById(EVENT_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should not update event if id was not found`() {
        every { mockRepository.findById(EVENT_ID) } returns Optional.empty()

        val actual = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should update event`() {
        every { mockRepository.findById(EVENT_ID) } returns Optional.of(DETAILED_EVENT_ENTITY)
        every { mockMapper.updateToEntity(DETAILED_EVENT_ENTITY, UPDATE_EVENT) } returns DETAILED_EVENT_ENTITY
        every { mockRepository.save(DETAILED_EVENT_ENTITY) } returns DETAILED_EVENT_ENTITY
        every { mockMapper.entityToDetailedModel(DETAILED_EVENT_ENTITY) } returns DETAILED_EVENT

        val actual = underTest.updateEvent(EVENT_ID, UPDATE_EVENT)

        actual shouldBePresent { it shouldBeEqual DETAILED_EVENT }
    }

    @Test
    internal fun `should mark the entity deleted`() {
        val entity = EventEntity(url = "url", title = "title").apply { id = UUID.randomUUID() }
        every { mockRepository.findById(EVENT_ID) } returns Optional.of(entity)
        every { mockRepository.save(entity) } returns entity

        underTest.removeEvent(EVENT_ID)

        entity.deletedAt.shouldNotBeNull()
    }

    @Test
    internal fun `should do nothing when deleting a missing entity`() {
        every { mockRepository.findById(EVENT_ID) } returns Optional.empty()

        underTest.removeEvent(EVENT_ID)
    }

    @Test
    internal fun `should return deleted events`() {
        every { mockRepository.findAllDeleted(PAGEABLE) } returns PageImpl(listOf(EVENT_ENTITY))
        every { mockMapper.entityToModel(EVENT_ENTITY) } returns EVENT

        val actual = underTest.findDeletedEvents(PAGEABLE)

        actual shouldBeEqual PageImpl(listOf(EVENT))
    }

    @Test
    internal fun `should restore a deleted event`() {
        val entity =
            EventEntity(url = "url", title = "title").apply {
                id = UUID.randomUUID()
                deletedAt = Instant.now()
            }
        every { mockRepository.findDeletedById(EVENT_ID) } returns Optional.of(entity)
        every { mockRepository.save(entity) } returns entity
        every { mockMapper.entityToDetailedModel(entity) } returns DETAILED_EVENT

        val actual = underTest.restoreEvent(EVENT_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_EVENT }
        entity.deletedAt.shouldBeNull()
    }

    @Test
    internal fun `should not restore a event that is not deleted`() {
        every { mockRepository.findDeletedById(EVENT_ID) } returns Optional.empty()

        val actual = underTest.restoreEvent(EVENT_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should return the revisions of a event`() {
        val revisions = Revisions.none<Long, EventEntity>()
        every { mockRepository.findRevisions(EVENT_ID) } returns revisions
        every { mockRevisionMapper.revisionsToModel(revisions, any<(EventEntity) -> Instant?>()) } returns REVISION_INFOS

        val actual = underTest.findEventRevisions(EVENT_ID)

        actual shouldBeEqual REVISION_INFOS
    }

    @Test
    internal fun `should return a event as it was at a revision`() {
        every { mockRepository.findRevision(EVENT_ID, REVISION) } returns Optional.of(Revision.of(REVISION_METADATA, EVENT_ENTITY))
        every { mockMapper.entityToModel(EVENT_ENTITY) } returns EVENT

        val actual = underTest.findEventRevision(EVENT_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual EVENT }
    }

    @Test
    internal fun `should give a event the fields it had at a revision`() {
        val current = EventEntity(url = "url", title = "title").apply { id = UUID.randomUUID() }
        every { mockRepository.findRevision(EVENT_ID, REVISION) } returns Optional.of(Revision.of(REVISION_METADATA, EVENT_ENTITY))
        every { mockMapper.entityToUpdate(EVENT_ENTITY) } returns UPDATE_EVENT
        every { mockMapper.updateToEntity(current, UPDATE_EVENT) } returns current
        every { mockRepository.findById(EVENT_ID) } returns Optional.of(current)
        every { mockRepository.save(current) } returns current
        every { mockMapper.entityToDetailedModel(current) } returns DETAILED_EVENT

        val actual = underTest.restoreEventRevision(EVENT_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual DETAILED_EVENT }
    }

    @Test
    internal fun `should not restore a revision that is missing`() {
        every { mockRepository.findRevision(EVENT_ID, REVISION) } returns Optional.empty()

        val actual = underTest.restoreEventRevision(EVENT_ID, REVISION)

        actual.shouldBeEmpty()
    }

    private companion object {
        private const val REVISION = 3L
        private val REVISION_METADATA = mockk<RevisionMetadata<Long>>()
        private val REVISION_INFOS = listOf(mockk<RevisionInfo>())
        private val PAGEABLE = Pageable.unpaged()
        private val EVENT_ID = mockk<UUID>()
        private val EVENT = mockk<Event>()
        private val EVENT_ENTITY = mockk<EventEntity>()
        private val CREATE_EVENT = mockk<CreateEvent>()
        private val UPDATE_EVENT = mockk<UpdateEvent>()
        private val DETAILED_EVENT = mockk<DetailedEvent>()
        private val DETAILED_EVENT_ENTITY = mockk<EventEntity>()
    }
}
