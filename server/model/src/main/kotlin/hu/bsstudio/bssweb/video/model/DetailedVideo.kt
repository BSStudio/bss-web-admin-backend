package hu.bsstudio.bssweb.video.model

import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import java.time.LocalDate
import java.util.UUID

data class DetailedVideo(
    val id: UUID,
    val urls: List<String>,
    val title: String,
    val description: String,
    val shootingDateStart: LocalDate,
    val shootingDateEnd: LocalDate,
    val labels: List<String>,
    val visible: Boolean,
    val crew: List<VideoCrew>,
)
