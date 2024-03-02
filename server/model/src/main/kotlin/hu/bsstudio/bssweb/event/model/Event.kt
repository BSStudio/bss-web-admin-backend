package hu.bsstudio.bssweb.event.model

import java.time.LocalDate
import java.util.UUID

data class Event(
        val id: UUID,
        val url: String,
        val title: String,
        val description: String,
        val dateFrom: LocalDate,
        val visible: Boolean,
)
