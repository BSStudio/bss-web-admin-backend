package hu.bsstudio.bssweb.ui.config

import jakarta.servlet.http.HttpServletRequest

/** The REST API's addresses. The UI's language and address conventions leave them alone. */
internal object ApiPaths {
    const val PATTERN = "/api/**"

    /** Whether [uri], the [request]'s own address by default, is one of the API's. */
    fun matches(
        request: HttpServletRequest,
        uri: String = request.requestURI,
    ): Boolean = uri.removePrefix(request.contextPath).startsWith("/api/")
}
