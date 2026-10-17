package hu.bsstudio.bssweb.video.page

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/** The new video form: only the title, the rest is filled in on the edit page. */
data class NewVideoForm(
    @field:NotBlank
    @field:Size(max = VideoForm.TITLE_LENGTH)
    val title: String = "",
)
