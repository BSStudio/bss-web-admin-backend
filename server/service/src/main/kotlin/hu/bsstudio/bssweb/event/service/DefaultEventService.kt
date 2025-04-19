package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.mapper.EventMapper
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.repository.DetailedEventRepository
import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Transactional
open class DefaultEventService(
    private val repository: SimpleEventRepository,
    private val detailedRepository: DetailedEventRepository,
    private val mapper: EventMapper,
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
        detailedRepository
            .findById(eventId)
            .map(mapper::entityToModel)

    override fun updateEvent(
        eventId: UUID,
        updateEvent: UpdateEvent,
    ): Optional<DetailedEvent> =
        detailedRepository
            .findById(eventId)
            .map { mapper.updateToEntity(it, updateEvent) }
            .map(detailedRepository::save)
            .map(mapper::entityToModel)

    override fun removeEvent(eventId: UUID) = repository.deleteById(eventId)
}
