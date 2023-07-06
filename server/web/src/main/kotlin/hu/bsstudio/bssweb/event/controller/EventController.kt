package hu.bsstudio.bssweb.event.controller

import hu.bsstudio.bssweb.event.api.EventApi
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class EventController(private val service: EventService): EventApi {

    override fun findAllEvent(): ResponseEntity<List<Event>> {
        return service.findAllEvent()
            .let { ResponseEntity.ok(it) }
    }

    override fun createEvent(createEvent: CreateEvent): ResponseEntity<Event> {
        return service.insertEvent(createEvent)
            .let { ResponseEntity(it, HttpStatus.CREATED) }
    }

    override fun findEventById(eventId: UUID): ResponseEntity<DetailedEvent> {
        return service.findEventById(eventId)
            .let { ResponseEntity.of(it) }
    }

    override fun updateEvent(eventId: UUID, updateEvent: UpdateEvent): ResponseEntity<DetailedEvent> {
        return service.updateEvent(eventId, updateEvent)
            .let { ResponseEntity.of(it) }
    }

    override fun deleteEvent(eventId: UUID): ResponseEntity<Void> {
        service.removeEvent(eventId)
        return ResponseEntity.noContent().build()
    }
}
