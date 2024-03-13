package hu.bsstudio.bssweb.video.model

import java.time.LocalDate
import java.util.UUID

data class Video(
    val id: UUID,
    val title: String,
    val urls: List<String>,
    val description: String,
    val shootingDateStart: LocalDate,
    val shootingDateEnd: LocalDate,
    val visible: Boolean,
)
