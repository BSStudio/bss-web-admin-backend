package hu.bsstudio.bssweb.video.model

import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import java.time.LocalDate

data class DetailedVideo(
    val id: String,
    val title: String,
    val description: String,
    val videoUrl: String?,
    val thumbnailUrl: String?,
    val uploadedAt: LocalDate,
    val visible: Boolean,
    val archived: Boolean,
    val crew: List<SimpleCrew>
)
