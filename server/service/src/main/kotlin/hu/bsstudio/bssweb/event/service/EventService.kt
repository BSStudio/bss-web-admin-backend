package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface EventService {
    fun findAllEvent(): List<Event>

    fun insertEvent(createEvent: CreateEvent): Event

    fun findEventById(eventId: UUID): Optional<DetailedEvent>

    /** Shows or hides the events, and returns the ids of the ones it found, like `VideoService.changeVideoVisibility`. */
    fun changeEventVisibility(
        eventIds: List<UUID>,
        visible: Boolean,
    ): List<UUID>

    fun updateEvent(
        eventId: UUID,
        updateEvent: UpdateEvent,
    ): Optional<DetailedEvent>

    fun removeEvent(eventId: UUID)

    fun findDeletedEvents(pageable: Pageable): Page<Event>

    /** Brings back a deleted event, or returns empty if no deleted event has the id. */
    fun restoreEvent(eventId: UUID): Optional<DetailedEvent>

    /** The changes of the event, newest first, including the ones made while it was deleted. */
    fun findEventRevisions(eventId: UUID): List<RevisionInfo>

    /** The event as it was at [revision]. Its videos have no history, so the video count is 0. */
    fun findEventRevision(
        eventId: UUID,
        revision: Long,
    ): Optional<Event>

    /** Gives the event the fields it had at [revision]. A deleted event has to be restored first. */
    fun restoreEventRevision(
        eventId: UUID,
        revision: Long,
    ): Optional<DetailedEvent>
}
