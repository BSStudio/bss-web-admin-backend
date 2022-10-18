package hu.bsstudio.bssweb.eventvideo.controller

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.eventvideo.api.EventVideoApi
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/eventVideo")
class EventVideoController(private val service: EventVideoService) : EventVideoApi {

    override fun addVideoToEvent(eventId: UUID, videoId: UUID): ResponseEntity<DetailedEvent> {
        return service.addVideoToEvent(eventId, videoId)
            .let { ResponseEntity.of(it) }
    }

    override fun removeVideoFromEvent(eventId: UUID, videoId: UUID): ResponseEntity<DetailedEvent> {
        return service.removeVideoFromEvent(eventId, videoId)
            .let { ResponseEntity.of(it) }
    }
}
