package hu.bsstudio.bssweb.event.model

import java.time.LocalDate

data class UpdateEvent(
    val url: String,
    val title: String,
    val description: String,
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val visible: Boolean,
)
