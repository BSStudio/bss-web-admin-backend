package hu.bsstudio.bssweb.video.model

import java.time.LocalDate

data class UpdateVideo(
    val title: String,
    val urls: List<String>,
    val description: String,
    val shootingDateStart: LocalDate,
    val shootingDateEnd: LocalDate,
    val visible: Boolean,
    val labels: List<String>,
)
