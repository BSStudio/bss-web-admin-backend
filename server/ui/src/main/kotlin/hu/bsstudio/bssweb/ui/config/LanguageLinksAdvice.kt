package hu.bsstudio.bssweb.ui.config

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.util.UriComponentsBuilder

/**
 * Puts the top bar's language links in every page's model as `languageLinks` (language → address): the current
 * address with only `lang` changed, so switching keeps the search, sort and page. An error page links to the
 * address that failed. admin.js takes the current address on click instead, since htmx may have changed it.
 */
@ConditionalOnAdminUi
@ControllerAdvice
class LanguageLinksAdvice {
    @ModelAttribute("languageLinks")
    fun languageLinks(request: HttpServletRequest): Map<String, String> {
        val failed = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as String?
        val address = UriComponentsBuilder.fromPath(failed ?: request.requestURI)
        // an error page's own query belongs to the error dispatch, not to the address that failed
        request.queryString?.takeIf { failed == null }?.let(address::query)
        return LANGUAGES.associateWith { language ->
            address
                .cloneBuilder()
                .replaceQueryParam(UiWebConfig.LOCALE_PARAM, language)
                .build()
                .toUriString()
        }
    }

    private companion object {
        private val LANGUAGES = listOf("en", "hu")
    }
}
