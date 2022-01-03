package hu.bsstudio.bssweb.event.model

import java.time.LocalDate

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val date: LocalDate,
    val archived: Boolean,
)
