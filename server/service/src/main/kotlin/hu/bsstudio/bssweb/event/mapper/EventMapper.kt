package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.video.mapper.VideoMapper

class EventMapper(
    private val videoMapper: VideoMapper,
) {
    fun entityToModel(entity: EventEntity): Event =
        Event(
            id = entity.id,
            url = entity.url,
            title = entity.title,
            description = entity.description,
            dateFrom = entity.dateFrom,
            dateTo = entity.dateTo,
            visible = entity.visible,
            videoCount = entity.videoCount,
        )

    fun entityToDetailedModel(entity: EventEntity): DetailedEvent =
        DetailedEvent(
            id = entity.id,
            url = entity.url,
            title = entity.title,
            description = entity.description,
            dateFrom = entity.dateFrom,
            dateTo = entity.dateTo,
            visible = entity.visible,
            videos = entity.videos.map(videoMapper::entityToModel),
        )

    fun modelToEntity(model: CreateEvent): EventEntity =
        EventEntity(
            url = model.url,
            title = model.title,
        )

    fun updateToEntity(
        eventEntity: EventEntity,
        updateEvent: UpdateEvent,
    ): EventEntity {
        eventEntity.url = updateEvent.url
        eventEntity.title = updateEvent.title
        eventEntity.description = updateEvent.description
        eventEntity.dateFrom = updateEvent.dateFrom
        eventEntity.dateTo = updateEvent.dateTo
        eventEntity.visible = updateEvent.visible
        return eventEntity
    }
}
