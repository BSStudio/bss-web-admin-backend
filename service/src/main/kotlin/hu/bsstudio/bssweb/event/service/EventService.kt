package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import java.util.Optional

interface EventService {
    fun findAllEvent(): List<Event>
    fun insertEvent(createEvent: CreateEvent): Event
    fun archiveEvents(eventIds: List<String>, archived: Boolean = true): List<String>
    fun findEventById(eventId: String): Optional<DetailedEvent>
    fun updateEvent(eventId: String, updateEvent: UpdateEvent): Optional<DetailedEvent>
    fun removeEvent(eventId: String)
}
