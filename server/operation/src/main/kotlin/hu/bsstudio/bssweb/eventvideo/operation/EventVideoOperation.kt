package hu.bsstudio.bssweb.eventvideo.operation

import hu.bsstudio.bssweb.event.model.DetailedEvent
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import java.util.*

@HttpExchange("/api/v1/eventVideo")
interface EventVideoOperation {
    @PostExchange
    fun addVideoToEvent(
        @RequestParam eventId: UUID,
        @RequestParam videoId: UUID,
    ): ResponseEntity<DetailedEvent>

    @DeleteExchange
    fun removeVideoFromEvent(
        @RequestParam eventId: UUID,
        @RequestParam videoId: UUID,
    ): ResponseEntity<DetailedEvent>
}
