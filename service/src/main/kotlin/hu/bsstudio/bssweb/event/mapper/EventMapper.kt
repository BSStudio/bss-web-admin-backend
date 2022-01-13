package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.video.mapper.VideoMapper

class EventMapper(private val videoMapper: VideoMapper) {

    fun entityToModel(entity: EventEntity): Event {
        return Event(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            date = entity.date,
            archived = entity.archived,
        )
    }

    fun entityToModel(entity: DetailedEventEntity): DetailedEvent {
        return DetailedEvent(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            date = entity.date,
            archived = entity.archived,
            videos = entity.videos.map(videoMapper::entityToModel)
        )
    }

    fun modelToEntity(model: CreateEvent): EventEntity {
        return EventEntity(
            id = model.id,
            name = model.name,
        )
    }
}
