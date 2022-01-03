package hu.bsstudio.bssweb.event.service

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import java.util.Optional

interface EventService {
    fun findAllEvent(): List<Event>
    fun insertEvent(createEvent: CreateEvent): Event
    fun archiveEvents(eventIds: List<String>, unArchive: Boolean): List<String>
    fun findEventById(eventId: String): Optional<Event>
    fun updateEvent(eventId: String, updateEvent: UpdateEvent): Optional<Event>
    fun removeEvent(eventId: String)
}
