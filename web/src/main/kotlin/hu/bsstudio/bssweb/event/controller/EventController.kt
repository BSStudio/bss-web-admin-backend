package hu.bsstudio.bssweb.event.controller

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.event.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/event")
class EventController(val service: EventService) {

    @GetMapping
    fun findAllEvent(): ResponseEntity<List<Event>> {
        return service.findAllEvent()
            .let { ResponseEntity.ok(it) }
    }

    @PostMapping
    fun createEvent(@RequestBody createEvent: CreateEvent): ResponseEntity<Event> {
        return service.insertEvent(createEvent)
            .let { ResponseEntity(it, HttpStatus.CREATED) }
    }

    @PutMapping("/archive")
    fun archiveEvents(
        @RequestParam eventIds: List<String>,
        @RequestParam unArchive: Boolean
    ): ResponseEntity<List<String>> {
        return service.archiveEvents(eventIds, unArchive)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{eventId}")
    fun findEventById(@PathVariable eventId: String): ResponseEntity<Event> {
        return service.findEventById(eventId)
            .let { ResponseEntity.of(it) }
    }

    @PutMapping("/{eventId}")
    fun updateEvent(
        @PathVariable eventId: String,
        @RequestBody updateEvent: UpdateEvent
    ): ResponseEntity<Event> {
        return service.updateEvent(eventId, updateEvent)
            .let { ResponseEntity.of(it) }
    }

    @DeleteMapping("/{eventId}")
    fun deleteEvent(@PathVariable eventId: String) {
        service.removeEvent(eventId)
    }
}
