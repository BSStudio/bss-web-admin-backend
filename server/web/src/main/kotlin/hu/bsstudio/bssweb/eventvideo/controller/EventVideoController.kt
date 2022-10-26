package hu.bsstudio.bssweb.eventvideo.controller

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/eventVideo")
class EventVideoController(private val service: EventVideoService) {

    @PostMapping
    fun addVideoToEvent(@RequestParam eventId: UUID, @RequestParam videoId: UUID): ResponseEntity<DetailedEvent> {
        return service.addVideoToEvent(eventId, videoId)
            .let { ResponseEntity.of(it) }
    }

    @DeleteMapping
    fun removeVideoFromEvent(@RequestParam eventId: UUID, @RequestParam videoId: UUID): ResponseEntity<DetailedEvent> {
        return service.removeVideoFromEvent(eventId, videoId)
            .let { ResponseEntity.of(it) }
    }
}
