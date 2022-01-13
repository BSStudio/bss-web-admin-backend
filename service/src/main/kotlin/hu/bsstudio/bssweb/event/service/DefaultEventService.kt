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
import java.util.Optional

class DefaultEventService(
    private val repository: EventRepository,
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

    override fun archiveEvents(eventIds: List<String>, archived: Boolean): List<String> {
        return repository.findAllById(eventIds)
            .map { it.copy(archived = archived) }
            .map(repository::save)
            .map(EventEntity::id)
    }

    override fun findEventById(eventId: String): Optional<DetailedEvent> {
        return detailedRepository.findById(eventId)
            .map(mapper::entityToModel)
    }

    override fun updateEvent(eventId: String, updateEvent: UpdateEvent): Optional<DetailedEvent> {
        return detailedRepository.findById(eventId)
            .map { updateEvent(it, updateEvent) }
            .map(detailedRepository::save)
            .map(mapper::entityToModel)
    }

    override fun removeEvent(eventId: String) = repository.deleteById(eventId)

    private fun updateEvent(eventEntity: DetailedEventEntity, updateEvent: UpdateEvent): DetailedEventEntity {
        return eventEntity.copy(
            name = updateEvent.name,
            description = updateEvent.description,
            date = updateEvent.date,
            archived = updateEvent.archived,
        )
    }
}
