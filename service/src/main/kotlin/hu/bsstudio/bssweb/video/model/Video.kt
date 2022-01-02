package hu.bsstudio.bssweb.video.model

import java.time.LocalDate

data class Video(
    val id: String,
    val title: String,
    val uploadedAt: LocalDate,
    val visible: Boolean,
    val archived: Boolean
)
