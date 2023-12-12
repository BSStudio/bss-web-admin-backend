package hu.bsstudio.bssweb.eventvideo.service

import hu.bsstudio.bssweb.event.model.DetailedEvent
import java.util.Optional
import java.util.UUID

interface EventVideoService {
    fun addVideoToEvent(
        eventId: UUID,
        videoId: UUID,
    ): Optional<DetailedEvent>

    fun removeVideoFromEvent(
        eventId: UUID,
        videoId: UUID,
    ): Optional<DetailedEvent>
}
