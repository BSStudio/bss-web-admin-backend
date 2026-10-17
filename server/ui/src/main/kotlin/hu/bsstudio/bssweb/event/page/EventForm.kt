package hu.bsstudio.bssweb.event.page

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.UpdateEvent
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

/**
 * The event edit form's fields. Every field has a default, so a field missing from the request fails validation
 * instead of the binding. The limits are the database's column lengths.
 */
data class EventForm(
    @field:NotBlank
    @field:Size(max = TITLE_LENGTH)
    val title: String = "",
    @field:NotBlank
    @field:Size(max = URL_LENGTH)
    val url: String = "",
    @param:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull
    val dateFrom: LocalDate? = null,
    @param:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull
    val dateTo: LocalDate? = null,
    @field:Size(max = DESCRIPTION_LENGTH)
    val description: String = "",
    val visible: Boolean = false,
) {
    /** An event may end on the day it starts, but not before. The form shows the error at the end date. */
    @get:AssertTrue
    val datesInOrder: Boolean
        get() = dateFrom == null || dateTo == null || !dateTo.isBefore(dateFrom)

    /** Call only on a valid form. */
    fun toUpdateEvent() =
        UpdateEvent(
            url = url.trim(),
            title = title.trim(),
            description = description.trim(),
            dateFrom = requireNotNull(dateFrom),
            dateTo = requireNotNull(dateTo),
            visible = visible,
        )

    companion object {
        const val TITLE_LENGTH = 250
        const val URL_LENGTH = 250
        private const val DESCRIPTION_LENGTH = 8192

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
