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
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.Optional
import java.util.UUID

@Transactional
class DefaultEventService(
    private val repository: EventRepository,
    private val mapper: EventMapper,
    private val revisionMapper: RevisionMapper,
) : EventService {
    override fun findAllEvent(): List<Event> =
        repository
            .findAll()
            .map(mapper::entityToModel)

    override fun insertEvent(createEvent: CreateEvent): Event =
        createEvent
            .let(mapper::modelToEntity)
            .let(repository::save)
            .let(mapper::entityToModel)

    override fun findEventById(eventId: UUID): Optional<DetailedEvent> =
        repository
            .findById(eventId)
            .map(mapper::entityToDetailedModel)

    override fun changeEventVisibility(
        eventIds: List<UUID>,
        visible: Boolean,
    ): List<UUID> =
        repository
            .findAllById(eventIds)
            .map {
                it.visible = visible
                it
            }.map(repository::save)
            .map { it.id }

    override fun updateEvent(
        eventId: UUID,
        updateEvent: UpdateEvent,
    ): Optional<DetailedEvent> =
        repository
            .findById(eventId)
            .map { mapper.updateToEntity(it, updateEvent) }
            .map(repository::save)
            .map(mapper::entityToDetailedModel)

    override fun removeEvent(eventId: UUID) =
        repository.findById(eventId).ifPresent {
            it.deletedAt = Instant.now()
            repository.save(it)
        }

    override fun findDeletedEvents(pageable: Pageable): Page<Event> =
        repository
            .findAllDeleted(pageable)
            .map(mapper::entityToModel)

    override fun restoreEvent(eventId: UUID): Optional<DetailedEvent> =
        repository
            .findDeletedById(eventId)
            .map {
                it.deletedAt = null
                it
            }.map(repository::save)
            .map(mapper::entityToDetailedModel)

    override fun findEventRevisions(eventId: UUID): List<RevisionInfo> =
        revisionMapper.revisionsToModel(repository.findRevisions(eventId), EventEntity::deletedAt)

    override fun findEventRevision(
        eventId: UUID,
        revision: Long,
    ): Optional<Event> =
        repository
            .findRevision(eventId, revision)
            .map { mapper.entityToModel(it.entity) }

    override fun restoreEventRevision(
        eventId: UUID,
        revision: Long,
    ): Optional<DetailedEvent> =
        repository
            .findRevision(eventId, revision)
            .flatMap { updateEvent(eventId, mapper.entityToUpdate(it.entity)) }
}
