package hu.bsstudio.bssweb.eventvideo.controller

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.eventvideo.operation.EventVideoOperation
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class EventVideoController(
    private val service: EventVideoService,
) : EventVideoOperation {
    override fun addVideoToEvent(
        eventId: UUID,
        videoId: UUID,
    ): ResponseEntity<DetailedEvent> =
        service
            .addVideoToEvent(eventId, videoId)
            .let { ResponseEntity.of(it) }

    override fun removeVideoFromEvent(
        eventId: UUID,
        videoId: UUID,
    ): ResponseEntity<DetailedEvent> =
        service
            .removeVideoFromEvent(eventId, videoId)
            .let { ResponseEntity.of(it) }
}
