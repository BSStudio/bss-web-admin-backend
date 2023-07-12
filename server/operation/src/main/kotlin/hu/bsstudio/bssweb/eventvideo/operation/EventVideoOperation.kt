package hu.bsstudio.bssweb.eventvideo.operation

import hu.bsstudio.bssweb.event.model.DetailedEvent
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@RequestMapping("/api/v1/eventVideo")
interface EventVideoOperation {

    @PostMapping
    fun addVideoToEvent(@RequestParam eventId: UUID, @RequestParam videoId: UUID): ResponseEntity<DetailedEvent>

    @DeleteMapping
    fun removeVideoFromEvent(@RequestParam eventId: UUID, @RequestParam videoId: UUID): ResponseEntity<DetailedEvent>
}
