package hu.bsstudio.bssweb.video.page

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

/**
 * The edit form's fields. URLs are edited as one textarea with one URL per line.
 * Every field has a default, so a field missing from the request fails validation instead of the binding.
 * The limits are the database's column lengths.
 */
data class VideoForm(
    @field:NotBlank
    @field:Size(max = TITLE_LENGTH)
    val title: String = "",
    val urls: String = "",
    @field:Size(max = DESCRIPTION_LENGTH)
    val description: String = "",
    @param:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull
    val shootingDateStart: LocalDate? = null,
    @param:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull
    val shootingDateEnd: LocalDate? = null,
    val visible: Boolean = false,
    val labels: List<String> = emptyList(),
) {
    /** The shooting may end on the day it started, but not before. The form shows the error at the end date. */
    @get:AssertTrue
    val datesInOrder: Boolean
        get() = shootingDateStart == null || shootingDateEnd == null || !shootingDateEnd.isBefore(shootingDateStart)

    /** Call only on a valid form. */
    fun toUpdateVideo() =
        UpdateVideo(
            title = title.trim(),
            urls = urls.lines().map(String::trim).filter(String::isNotEmpty),
            description = description.trim(),
            shootingDateStart = requireNotNull(shootingDateStart),
            shootingDateEnd = requireNotNull(shootingDateEnd),
            visible = visible,
            labels = labels,
        )

    companion object {
        const val TITLE_LENGTH = 250
        private const val DESCRIPTION_LENGTH = 2000

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
