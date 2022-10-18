package hu.bsstudio.bssweb.event.model

import java.time.LocalDate

data class UpdateEvent(
    val url: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val visible: Boolean,
)
