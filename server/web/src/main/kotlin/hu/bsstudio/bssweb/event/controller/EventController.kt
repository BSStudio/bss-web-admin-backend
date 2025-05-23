package hu.bsstudio.bssweb.event.controller

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.operation.EventOperation
import hu.bsstudio.bssweb.event.service.EventService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.UUID

@RestController
class EventController(
    private val service: EventService,
) : EventOperation {
    override fun findAllEvent(): ResponseEntity<List<Event>> =
        service
            .findAllEvent()
            .let { ResponseEntity.ok(it) }

    override fun createEvent(createEvent: CreateEvent): ResponseEntity<Event> =
        service
            .insertEvent(createEvent)
            .let { ResponseEntity.created(locationUri(it.id)).body(it) }

    override fun findEventById(eventId: UUID): ResponseEntity<DetailedEvent> =
        service
            .findEventById(eventId)
            .let { ResponseEntity.of(it) }

    override fun updateEvent(
        eventId: UUID,
        updateEvent: UpdateEvent,
    ): ResponseEntity<DetailedEvent> =
        service
            .updateEvent(eventId, updateEvent)
            .let { ResponseEntity.of(it) }

    override fun deleteEvent(eventId: UUID): ResponseEntity<Void> {
        service.removeEvent(eventId)
        return ResponseEntity.noContent().build()
    }

    private fun locationUri(id: UUID) =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
}
