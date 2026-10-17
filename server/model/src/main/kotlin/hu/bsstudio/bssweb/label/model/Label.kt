package hu.bsstudio.bssweb.label.model

import java.util.UUID

data class Label(
    val id: UUID,
    val name: String,
    val description: String,
    /** How many videos have the label. */
    val videoCount: Long = 0,
)
