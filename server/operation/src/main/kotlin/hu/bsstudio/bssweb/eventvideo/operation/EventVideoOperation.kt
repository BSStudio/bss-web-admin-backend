package hu.bsstudio.bssweb.eventvideo.operation

import hu.bsstudio.bssweb.event.model.DetailedEvent
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

interface EventVideoOperation {

    @PostMapping("/api/v1/eventVideo")
    fun addVideoToEvent(@RequestParam eventId: UUID, @RequestParam videoId: UUID): ResponseEntity<DetailedEvent>

    @DeleteMapping("/api/v1/eventVideo")
    fun removeVideoFromEvent(@RequestParam eventId: UUID, @RequestParam videoId: UUID): ResponseEntity<DetailedEvent>
}
