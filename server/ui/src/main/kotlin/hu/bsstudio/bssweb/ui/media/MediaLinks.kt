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

    /** A video's thumbnail, for example `…/v/{id}/thumbnail/hd.webp`. */
    fun video(videoId: UUID) = MediaImage("$baseUrl/v/$videoId/thumbnail", VIDEO_SIZES)

    /** A member's profile picture, for example `…/m/{id}/profile/m.webp`. */
    fun member(memberId: UUID) = MediaImage("$baseUrl/m/$memberId/profile", MEMBER_SIZES)

    private companion object {
        // the widths the file API resizes uploads to (it keeps the aspect ratio within 16:9 boxes)
        private val VIDEO_SIZES = mapOf("sd" to 854, "hd" to 1280, "fhd" to 1920)
        private val MEMBER_SIZES = mapOf("m" to 854, "l" to 1280, "xl" to 1920)
    }
}
