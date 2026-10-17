package hu.bsstudio.bssweb.event.page

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.UpdateEvent
import java.time.LocalDate

/** The event edit form's fields. Only the checkbox has a default, because browsers leave it out when unchecked. */
data class EventForm(
    val title: String,
    val url: String,
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val description: String,
    val visible: Boolean = false,
) {
    fun toUpdateEvent() =
        UpdateEvent(
            url = url.trim(),
            title = title.trim(),
            description = description.trim(),
            dateFrom = dateFrom,
            dateTo = dateTo,
            visible = visible,
        )

    companion object {
        fun from(event: DetailedEvent) =
            EventForm(
                title = event.title,
                url = event.url,
                dateFrom = event.dateFrom,
                dateTo = event.dateTo,
                description = event.description,
                visible = event.visible,
            )
    }
}
