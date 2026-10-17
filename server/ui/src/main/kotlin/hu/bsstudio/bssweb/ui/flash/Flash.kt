package hu.bsstudio.bssweb.ui.flash

import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * A one-off message shown at the top of the next page after a redirect.
 * [key] is a message key; [arg] fills its `{0}` placeholder.
 */
data class Flash(
    val key: String,
    val arg: Any? = null,
    val error: Boolean = false,
)

fun RedirectAttributes.flash(
    key: String,
    arg: Any? = null,
    error: Boolean = false,
) {
    addFlashAttribute("flash", Flash(key, arg, error))
}
