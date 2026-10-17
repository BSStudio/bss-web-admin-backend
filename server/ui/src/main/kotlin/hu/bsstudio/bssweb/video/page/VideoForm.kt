package hu.bsstudio.bssweb.video.page

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import java.time.LocalDate

/**
 * The edit form's fields. URLs are edited as one textarea with one URL per line.
 * Only checkboxes have defaults, because browsers leave unchecked ones out of the request.
 */
data class VideoForm(
    val title: String,
    val urls: String,
    val description: String,
    val shootingDateStart: LocalDate,
    val shootingDateEnd: LocalDate,
    val visible: Boolean = false,
    val labels: List<String> = emptyList(),
) {
    fun toUpdateVideo() =
        UpdateVideo(
            title = title.trim(),
            urls = urls.lines().map(String::trim).filter(String::isNotEmpty),
            description = description.trim(),
            shootingDateStart = shootingDateStart,
            shootingDateEnd = shootingDateEnd,
            visible = visible,
            labels = labels,
        )

    companion object {
        fun from(video: DetailedVideo) =
            VideoForm(
                title = video.title,
                urls = video.urls.joinToString("\n"),
                description = video.description,
                shootingDateStart = video.shootingDateStart,
                shootingDateEnd = video.shootingDateEnd,
                visible = video.visible,
                labels = video.labels,
            )
    }
}
