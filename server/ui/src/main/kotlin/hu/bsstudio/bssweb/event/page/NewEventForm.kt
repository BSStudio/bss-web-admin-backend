package hu.bsstudio.bssweb.event.page

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/** The new event form: the title and the address, the rest is filled in on the edit page. */
data class NewEventForm(
    @field:NotBlank
    @field:Size(max = EventForm.TITLE_LENGTH)
    val title: String = "",
    @field:NotBlank
    @field:Size(max = EventForm.URL_LENGTH)
    val url: String = "",
)
