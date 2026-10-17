package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import java.util.Optional
import java.util.UUID

interface EventService {
    fun findAllEvent(): List<Event>

    fun insertEvent(createEvent: CreateEvent): Event

    fun findEventById(eventId: UUID): Optional<DetailedEvent>

    /** Shows or hides the events in one update, without touching their other fields; returns how many it changed. */
    fun changeEventVisibility(
        eventIds: List<UUID>,
        visible: Boolean,
    ): Int

    fun updateEvent(
        eventId: UUID,
        updateEvent: UpdateEvent,
    ): Optional<DetailedEvent>

    fun removeEvent(eventId: UUID)
}
