package hu.bsstudio.bssweb.event.controller

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RequestMapping("/api/v1/event")
interface EventController {

    @GetMapping
    fun findAllEvent(): ResponseEntity<List<Event>>

    @PostMapping
    fun createEvent(@RequestBody createEvent: CreateEvent): ResponseEntity<Event>

    @GetMapping("/{eventId}")
    fun findEventById(@PathVariable eventId: UUID): ResponseEntity<DetailedEvent>

    @PutMapping("/{eventId}")
    fun updateEvent(@PathVariable eventId: UUID, @RequestBody updateEvent: UpdateEvent): ResponseEntity<DetailedEvent>

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEvent(@PathVariable eventId: UUID)
}
