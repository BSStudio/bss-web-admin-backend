package hu.bsstudio.bssweb.eventvideo.service

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import java.util.Optional
import java.util.UUID

class DefaultEventVideoService(
    private val repository: EventVideoRepository,
    private val eventService: EventService,
) : EventVideoService {
    override fun addVideoToEvent(
        eventId: UUID,
        videoId: UUID,
    ): Optional<DetailedEvent> =
        EventVideoEntity(eventId = eventId, videoId = videoId)
            .let(repository::save)
            .let(EventVideoEntity::eventId)
            .let(eventService::findEventById)

    override fun removeVideoFromEvent(
        eventId: UUID,
        videoId: UUID,
    ): Optional<DetailedEvent> =
        EventVideoEntity(eventId = eventId, videoId = videoId)
            .let(repository::deleteById)
            .run { eventService.findEventById(eventId) }
}
