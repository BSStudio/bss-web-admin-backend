package hu.bsstudio.bssweb.video.model

import java.time.LocalDate

data class UpdateVideo(
    val url: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val uploadedAt: LocalDate,
    val visible: Boolean,
)
