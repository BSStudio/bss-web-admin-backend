package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.event.mapper.EventMapper
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.repository.DetailedEventRepository
import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import jakarta.transaction.Transactional
import java.util.Optional
import java.util.UUID

@Transactional
open class DefaultEventService(
    private val repository: SimpleEventRepository,
    private val detailedRepository: DetailedEventRepository,
    private val mapper: EventMapper
) : EventService {

    override fun findAllEvent(): List<Event> {
        return repository.findAll()
            .map(mapper::entityToModel)
    }

    override fun insertEvent(createEvent: CreateEvent): Event {
        return createEvent
            .let(mapper::modelToEntity)
            .let(repository::save)
            .let(mapper::entityToModel)
    }

    override fun changeVisibility(eventIds: List<UUID>, visible: Boolean): List<UUID> {
        return repository.findAllById(eventIds)
            .map { it.visible = visible; it }
            .map(repository::save)
            .map(SimpleEventEntity::id)
    }

    override fun findEventById(eventId: UUID): Optional<DetailedEvent> {
        return detailedRepository.findById(eventId)
            .map(mapper::entityToModel)
    }

    override fun updateEvent(eventId: UUID, updateEvent: UpdateEvent): Optional<DetailedEvent> {
        return detailedRepository.findById(eventId)
            .map { mapper.updateToEntity(it, updateEvent) }
            .map(detailedRepository::save)
            .map(mapper::entityToModel)
    }

    override fun removeEvent(eventId: UUID) = repository.deleteById(eventId)
}
