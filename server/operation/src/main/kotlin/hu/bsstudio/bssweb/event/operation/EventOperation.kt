package hu.bsstudio.bssweb.event.operation

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@RequestMapping("/api/v1/event")
interface EventOperation {

    @GetMapping
    fun findAllEvent(): ResponseEntity<List<Event>>

    @PostMapping
    fun createEvent(@RequestBody createEvent: CreateEvent): ResponseEntity<Event>

    @GetMapping("/{eventId}")
    fun findEventById(@PathVariable eventId: UUID): ResponseEntity<DetailedEvent>

    @PutMapping("/{eventId}")
    fun updateEvent(@PathVariable eventId: UUID, @RequestBody updateEvent: UpdateEvent): ResponseEntity<DetailedEvent>

    @DeleteMapping("/{eventId}")
    fun deleteEvent(@PathVariable eventId: UUID): ResponseEntity<Void>
}
