package hu.bsstudio.bssweb.event.model

import hu.bsstudio.bssweb.video.model.Video
import java.time.LocalDate

data class DetailedEvent(
    val id: String,
    val name: String,
    val description: String,
    val date: LocalDate,
    val archived: Boolean,
    val videos: List<Video>
)
