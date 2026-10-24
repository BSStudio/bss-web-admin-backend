package hu.bsstudio.bssweb.ui.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.server.PathContainer
import org.springframework.web.util.pattern.PathPatternParser

/**
 * The admin UI is served from the root. The REST API, its documentation, the actuator and the WebJars keep their
 * own addresses; every other address is the UI's.
 */
object UiPaths {
    /** The addresses that are not the UI's. */
    val EXCLUDED =
        listOf(
            "/api/**",
            "/actuator/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/open-api.yaml",
            "/webjars/**",
        )

    private val excludedPatterns = EXCLUDED.map(PathPatternParser.defaultInstance::parse)

    /** Whether [path], without the context path, is one of the UI's. */
    fun isUi(path: String): Boolean {
        val container = PathContainer.parsePath(path)
        return excludedPatterns.none { it.matches(container) }
    }

    /** Whether the [request]'s address is one of the UI's. */
    fun isUi(request: HttpServletRequest): Boolean = isUi(request.requestURI.removePrefix(request.contextPath))
}
