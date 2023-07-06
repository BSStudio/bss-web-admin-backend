package hu.bsstudio.bssweb.eventvideo.api

import hu.bsstudio.bssweb.event.model.DetailedEvent
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/api/v1/eventVideo")
interface EventVideoApi {

    @PostMapping
    fun addVideoToEvent(@RequestParam eventId: UUID, @RequestParam videoId: UUID): ResponseEntity<DetailedEvent>

    @DeleteMapping
    fun removeVideoFromEvent(@RequestParam eventId: UUID, @RequestParam videoId: UUID): ResponseEntity<DetailedEvent>
}
