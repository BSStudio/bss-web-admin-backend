package hu.bsstudio.bssweb.event.controller

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
class DefaultEventController(private val service: EventService): EventController {

    override fun findAllEvent(): ResponseEntity<List<Event>> {
        return this.service.findAllEvent()
            .let { ResponseEntity.ok(it) }
    }

    override fun createEvent(createEvent: CreateEvent): ResponseEntity<Event> {
        return this.service.insertEvent(createEvent)
            .let { ResponseEntity(it, HttpStatus.CREATED) }
    }

    override fun findEventById(eventId: UUID): ResponseEntity<DetailedEvent> {
        return this.service.findEventById(eventId)
            .let { ResponseEntity.of(it) }
    }

    override fun updateEvent(eventId: UUID, updateEvent: UpdateEvent): ResponseEntity<DetailedEvent> {
        return this.service.updateEvent(eventId, updateEvent)
            .let { ResponseEntity.of(it) }
    }

    override fun deleteEvent(eventId: UUID) {
        this.service.removeEvent(eventId)
    }
}
