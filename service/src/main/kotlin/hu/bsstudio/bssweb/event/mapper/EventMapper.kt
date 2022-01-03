package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.Event

class EventMapper {

    fun entityToModel(entity: EventEntity): Event {
        return Event(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            date = entity.date,
            archived = entity.archived,
        )
    }

    fun modelToEntity(model: CreateEvent): EventEntity {
        return EventEntity(
            id = model.id,
            name = model.name,
        )
    }

    fun modelToEntity(model: Event): EventEntity {
        return EventEntity(
            id = model.id,
            name = model.name,
            description = model.description,
            date = model.date,
            archived = model.archived,
        )
    }
}
