package hu.bsstudio.bssweb.ui.error

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webmvc.error.DefaultErrorAttributes
import org.springframework.core.NestedExceptionUtils
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponse
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.ModelAndView
import java.net.URLEncoder
import java.time.Instant
import java.util.UUID

/**
 * Adds what an admin needs to report an error: the error message, a short reference that is logged
 * together with the stack trace, and a link that opens a pre-filled GitHub issue.
 * Only for admin pages and logged-in users; API responses and anonymous visitors keep Spring's defaults.
 */
@Component
class AdminErrorAttributes : DefaultErrorAttributes() {
    // the error page is rendered in a separate dispatch that reports GET, so remember the real method here
    override fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception,
    ): ModelAndView? {
        request.setAttribute(METHOD_ATTRIBUTE, request.method)
        return super.resolveException(request, response, handler, ex)
    }

    override fun getErrorAttributes(
        webRequest: WebRequest,
        options: ErrorAttributeOptions,
    ): MutableMap<String, Any?> {
        val attributes = super.getErrorAttributes(webRequest, options)
        val path = attributes["path"] as? String
        if (path == null || !path.startsWith(ADMIN_PATH) || webRequest.userPrincipal == null) {
            return attributes
        }
        val status = attributes["status"] as Int
        val reason = attributes["error"].toString()
        val error = getError(webRequest)
        val reference = UUID.randomUUID().toString().take(REFERENCE_LENGTH)
        val request = listOfNotNull(webRequest.getAttribute(METHOD_ATTRIBUTE, SCOPE_REQUEST), path).joinToString(" ")
        val message = (error as? ErrorResponse)?.body?.detail ?: error?.message ?: reason
        val trace = if (status >= SERVER_ERROR && error != null) shortTrace(error) else null

        if (status >= SERVER_ERROR) {
            logger.error("Admin UI error {} on {}", reference, request, error)
        } else {
            logger.info("Admin UI error {} ({}) on {}: {}", reference, status, request, message)
        }

        val details =
            listOfNotNull(
                "Status: $status $reason",
                "Request: $request",
                "Time: ${Instant.now()}",
                "Reference: $reference",
                "Message: $message",
                trace?.let { "\n$it" },
            ).joinToString("\n").take(MAX_DETAILS_LENGTH)

        attributes["reference"] = reference
        attributes["request"] = request
        attributes["message"] = message
        attributes["trace"] = trace
        attributes["reportUrl"] =
            issueUrl(
                "title" to "[Admin UI] $status $reason on $request",
                "page" to request,
                "reference" to reference,
                "details" to details,
            )
        return attributes
    }

    private fun shortTrace(error: Throwable) =
        NestedExceptionUtils
            .getMostSpecificCause(error)
            .stackTraceToString()
            .lineSequence()
            .take(MAX_TRACE_LINES)
            .joinToString("\n")

    private fun issueUrl(vararg fields: Pair<String, String>) =
        (listOf("template" to ISSUE_TEMPLATE) + fields)
            .joinToString("&", prefix = "$NEW_ISSUE_URL?") { (name, value) ->
                "$name=${URLEncoder.encode(value, Charsets.UTF_8)}"
            }

    private companion object {
        private val logger = LoggerFactory.getLogger(AdminErrorAttributes::class.java)
        private const val ADMIN_PATH = "/admin"
        private val METHOD_ATTRIBUTE = "${AdminErrorAttributes::class.java.name}.METHOD"
        private const val SERVER_ERROR = 500
        private const val REFERENCE_LENGTH = 8
        private const val MAX_TRACE_LINES = 15
        private const val MAX_DETAILS_LENGTH = 4000
        private const val NEW_ISSUE_URL = "https://github.com/BSStudio/bss-web-admin-backend/issues/new"
        private const val ISSUE_TEMPLATE = "admin-ui-error.yml"
    }
}
