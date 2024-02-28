package hu.bsstudio.bssweb.video.model

import java.time.LocalDate
import java.util.UUID

data class Video(
    val id: UUID,
    val urls: List<String>,
    val title: String,
    val uploadedAt: LocalDate,
    val visible: Boolean,
)
