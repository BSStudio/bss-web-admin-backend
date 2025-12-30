package hu.bsstudio.bssweb.event.operation

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.annotation.PutExchange
import java.util.UUID

@HttpExchange("/api/v1/event")
interface EventOperation {
    @GetExchange
    fun findAllEvent(): ResponseEntity<List<Event>>

    @PostExchange
    fun createEvent(
        @RequestBody createEvent: CreateEvent,
    ): ResponseEntity<Event>

    @GetExchange("/{eventId}")
    fun findEventById(
        @PathVariable eventId: UUID,
    ): ResponseEntity<DetailedEvent>

    @PutExchange("/{eventId}")
    fun updateEvent(
        @PathVariable eventId: UUID,
        @RequestBody updateEvent: UpdateEvent,
    ): ResponseEntity<DetailedEvent>

    @DeleteExchange("/{eventId}")
    fun deleteEvent(
        @PathVariable eventId: UUID,
    ): ResponseEntity<Unit>
}
