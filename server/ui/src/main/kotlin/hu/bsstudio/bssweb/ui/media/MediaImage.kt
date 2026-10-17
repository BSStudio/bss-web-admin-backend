package hu.bsstudio.bssweb.ui.media

/**
 * An image the file API made in every size and format, written as `srcset` values for a `<picture>`.
 * The browser picks the first format it supports (AVIF, then WebP, then JPEG) and, with `sizes="auto"`,
 * the smallest size that is still sharp at the width the image is shown.
 *
 * [sizes] maps the file API's size names to their widths, smallest first.
 */
class MediaImage(
    private val folder: String,
    private val sizes: Map<String, Int>,
) {
    /** For example `…/sd.avif 854w, …/hd.avif 1280w, …/fhd.avif 1920w`. [version] busts the cache after an upload. */
    fun srcset(
        format: String,
        version: String?,
    ): String = sizes.entries.joinToString(", ") { (name, width) -> "$folder/$name.$format${version.orEmpty()} ${width}w" }

    /** The smallest JPEG, for browsers that ignore `srcset`. */
    fun fallback(version: String?): String = "$folder/${sizes.keys.first()}.jpeg${version.orEmpty()}"
}
