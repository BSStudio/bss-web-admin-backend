package hu.bsstudio.bssweb.video.model

import java.time.LocalDate

data class UpdateVideo(
    val urls: List<String>,
    val title: String,
    val description: String,
    val shootingDateStart: LocalDate,
    val shootingDateEnd: LocalDate,
    val labels: List<String>,
    val visible: Boolean,
)
