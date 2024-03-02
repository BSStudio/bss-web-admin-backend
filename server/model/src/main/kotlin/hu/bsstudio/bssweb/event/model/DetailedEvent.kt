package hu.bsstudio.bssweb.event.model

import hu.bsstudio.bssweb.video.model.Video
import java.time.LocalDate
import java.util.UUID

data class DetailedEvent(
    val id: UUID,
    val url: String,
    val title: String,
    val description: String,
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val visible: Boolean,
    val videos: List<Video>,
)
