package hu.bsstudio.bssweb.video.model

import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import java.time.LocalDate
import java.util.UUID

data class DetailedVideo(
    val id: UUID,
    val title: String,
    val urls: List<String>,
    val description: String,
    val shootingDateStart: LocalDate,
    val shootingDateEnd: LocalDate,
    val visible: Boolean,
    val labels: List<String>,
    val crew: List<VideoCrew>,
)
