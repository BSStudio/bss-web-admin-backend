package hu.bsstudio.bssweb.ui.error

import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldStartWith
import jakarta.servlet.RequestDispatcher
import org.junit.jupiter.api.Test
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.server.ResponseStatusException
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.Principal
import java.sql.SQLException

internal class AdminErrorAttributesTest {
    private val underTest = AdminErrorAttributes()

    @Test
    internal fun `should leave errors outside the admin alone`() {
        attributesFor(request(path = "/api/v1/video")).keys shouldNotContain "reportUrl"
    }

    @Test
    internal fun `should leave errors without a path alone`() {
        attributesFor(request(path = null)).keys shouldNotContain "reportUrl"
    }

    @Test
    internal fun `should not show details to anonymous visitors`() {
        attributesFor(request(principal = null)).keys shouldNotContain "reportUrl"
    }

    @Test
    internal fun `should describe a server error with its root cause and a report link`() {
        val error = IllegalStateException("Database is down", SQLException("connection refused"))

        val attributes = attributesFor(request(error = error))

        val reference = attributes["reference"] as String
        reference shouldHaveLength 8
        attributes["request"] shouldBe "POST /admin/videos"
        attributes["message"] shouldBe "Database is down"
        attributes["trace"] as String shouldStartWith "java.sql.SQLException: connection refused"
        val reportUrl = attributes["reportUrl"] as String
        reportUrl shouldStartWith "https://github.com/BSStudio/bss-web-admin-backend/issues/new?template=admin-ui-error.yml&"
        reportUrl shouldContain "title=${encode("[Admin UI] 500 Internal Server Error on POST /admin/videos")}"
        reportUrl shouldContain "page=${encode("POST /admin/videos")}"
        reportUrl shouldContain "reference=$reference"
        val details = URLDecoder.decode(reportUrl.substringAfter("details="), Charsets.UTF_8)
        details shouldContain "Message: Database is down"
        details shouldContain "java.sql.SQLException: connection refused"
    }

    @Test
    internal fun `should use the reason of a client error and leave out the trace`() {
        val error = ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found")

        val attributes = attributesFor(request(status = 404, error = error))

        attributes["message"] shouldBe "Video not found"
        attributes["trace"] shouldBe null
    }

    @Test
    internal fun `should fall back to the exception message`() {
        val error = ResponseStatusException(HttpStatus.NOT_FOUND)

        attributesFor(request(status = 404, error = error))["message"] shouldBe "404 NOT_FOUND"
    }

    @Test
    internal fun `should fall back to the status and path when there is no exception`() {
        val attributes = attributesFor(request())

        attributes["request"] shouldBe "/admin/videos"
        attributes["message"] shouldBe "Internal Server Error"
        attributes["trace"] shouldBe null
    }

    private fun attributesFor(request: ServletWebRequest): Map<String, Any?> =
        underTest.getErrorAttributes(request, ErrorAttributeOptions.defaults())

    private fun request(
        path: String? = "/admin/videos",
        status: Int = 500,
        error: Exception? = null,
        principal: Principal? = TestingAuthenticationToken("jdoe", null),
    ): ServletWebRequest {
        val request = MockHttpServletRequest("POST", "/error")
        // an exception passes through Spring MVC's exception resolvers before the error page is rendered
        error?.let { underTest.resolveException(request, MockHttpServletResponse(), null, it) }
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status)
        path?.let { request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, it) }
        request.userPrincipal = principal
        return ServletWebRequest(request)
    }

    private fun encode(value: String) = URLEncoder.encode(value, Charsets.UTF_8)
}
