package hu.bsstudio.bssweb.label.model

import java.util.UUID

data class Label(
    val id: UUID,
    val name: String,
    val description: String,
)
