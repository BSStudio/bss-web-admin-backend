package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import java.util.UUID

class EventMapper(
    private val videoMapper: VideoMapper,
    private val idGenerator: () -> UUID = UUID::randomUUID
) {

    fun entityToModel(entity: EventEntity): Event {
        return Event(
            id = entity.id,
            url = entity.url,
            title = entity.title,
            description = entity.description,
            date = entity.date,
            visible = entity.visible,
        )
    }

    fun entityToModel(entity: DetailedEventEntity): DetailedEvent {
        return DetailedEvent(
            id = entity.id,
            url = entity.url,
            title = entity.title,
            description = entity.description,
            date = entity.date,
            visible = entity.visible,
            videos = entity.videos.map(videoMapper::entityToModel)
        )
    }

    fun modelToEntity(model: CreateEvent): EventEntity {
        return EventEntity(
            id = idGenerator.invoke(),
            url = model.url,
            title = model.title,
        )
    }
}
