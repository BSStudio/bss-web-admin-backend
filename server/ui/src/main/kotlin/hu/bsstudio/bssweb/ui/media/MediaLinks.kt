package hu.bsstudio.bssweb.ui.media

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import java.util.UUID

/**
 * Links to the images the file API writes, as `media` in every page's model.
 * `bss.media.url` is where those files are published; without it, pages show no images.
 */
@ControllerAdvice
class MediaLinks(
    @Value($$"${bss.media.url:}") baseUrl: String,
) {
    private val baseUrl = baseUrl.trimEnd('/')

    val enabled = this.baseUrl.isNotEmpty()

    @ModelAttribute("media")
    fun media() = this

    /** The folder with a video's thumbnail in every size and format, e.g. `…/hd.webp`. */
    fun video(videoId: UUID) = "$baseUrl/v/$videoId/thumbnail"

    /** The folder with a member's profile picture in every size and format, e.g. `…/m.webp`. */
    fun member(memberId: UUID) = "$baseUrl/m/$memberId/profile"
}
