package hu.bsstudio.bssweb.ui.config

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import java.util.Locale

/**
 * The admin keeps the chosen language in a cookie, English by default. Everything else, such as the REST API,
 * goes on reading `Accept-Language`, as Spring does without a resolver of its own. Spring has one locale resolver
 * for the whole app, so this one tells the two apart by the address, counting an error page as part of the page
 * that failed.
 */
class AdminLocaleResolver(
    cookieName: String,
) : LocaleResolver {
    private val admin = CookieLocaleResolver(cookieName).apply { setDefaultLocale(Locale.ENGLISH) }
    private val other = AcceptHeaderLocaleResolver()

    override fun resolveLocale(request: HttpServletRequest): Locale = resolverFor(request).resolveLocale(request)

    override fun setLocale(
        request: HttpServletRequest,
        response: HttpServletResponse?,
        locale: Locale?,
    ) = resolverFor(request).setLocale(request, response, locale)

    private fun resolverFor(request: HttpServletRequest): LocaleResolver {
        val uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as String? ?: request.requestURI
        return if (UiPaths.isUi(uri.removePrefix(request.contextPath))) admin else other
    }
}
