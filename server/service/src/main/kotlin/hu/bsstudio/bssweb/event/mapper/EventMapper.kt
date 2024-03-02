package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.video.mapper.VideoMapper

class EventMapper(private val videoMapper: VideoMapper) {
    fun entityToModel(entity: SimpleEventEntity): Event {
        return Event(
            id = entity.id,
            url = entity.url,
            title = entity.title,
            description = entity.description,
            dateFrom = entity.dateFrom,
            visible = entity.visible,
        )
    }

    fun entityToModel(entity: DetailedEventEntity): DetailedEvent {
        return DetailedEvent(
            id = entity.id,
            url = entity.url,
            title = entity.title,
            description = entity.description,
            dateFrom = entity.dateFrom,
            visible = entity.visible,
            videos = entity.videos.map(videoMapper::entityToModel),
        )
    }

    fun modelToEntity(model: CreateEvent): SimpleEventEntity {
        return SimpleEventEntity(
            url = model.url,
            title = model.title,
        )
    }

    fun updateToEntity(
        eventEntity: DetailedEventEntity,
        updateEvent: UpdateEvent,
    ): DetailedEventEntity {
        eventEntity.url = updateEvent.url
        eventEntity.title = updateEvent.title
        eventEntity.description = updateEvent.description
        eventEntity.dateFrom = updateEvent.dateFrom
        eventEntity.visible = updateEvent.visible
        return eventEntity
    }
}
