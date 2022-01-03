package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.mapper.EventMapper
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.repository.EventRepository
import java.util.Optional

class DefaultEventService(val repository: EventRepository) : EventService {

    internal var mapper: EventMapper = EventMapper()

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

    override fun archiveEvents(eventIds: List<String>, unArchive: Boolean): List<String> {
        return repository.findAllById(eventIds)
            .map { it.copy(archived = !unArchive) }
            .map(repository::save)
            .map(EventEntity::id)
    }

    override fun findEventById(eventId: String): Optional<Event> {
        return repository.findById(eventId)
            .map(mapper::entityToModel)
    }

    override fun updateEvent(eventId: String, updateEvent: UpdateEvent): Optional<Event> {
        return repository.findById(eventId)
            .map { it.copy() }
            .map(repository::save)
            .map(mapper::entityToModel)
    }

    override fun removeEvent(eventId: String) {
        repository.deleteById(eventId)
    }
}
