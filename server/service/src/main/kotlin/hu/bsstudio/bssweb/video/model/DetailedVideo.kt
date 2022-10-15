package hu.bsstudio.bssweb.video.model

import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import java.time.LocalDate
import java.util.UUID

data class DetailedVideo(
    val id: UUID,
    val url: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val uploadedAt: LocalDate,
    val visible: Boolean,
    val crew: List<SimpleCrew>
)
