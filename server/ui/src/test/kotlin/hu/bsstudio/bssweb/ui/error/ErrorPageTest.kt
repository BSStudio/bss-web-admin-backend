package hu.bsstudio.bssweb.ui.error

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.dashboard.page.DashboardPageController
import hu.bsstudio.bssweb.metrics.service.MetricsService
import hu.bsstudio.bssweb.ui.config.UiSecurityConfig
import jakarta.servlet.RequestDispatcher
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/** Calls the error page the way the servlet container does after an error, with the error in request attributes. */
@WebMvcTest(DashboardPageController::class)
@Import(UiSecurityConfig::class)
internal class ErrorPageTest(
    @param:Autowired private val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var service: MetricsService

    @Test
    internal fun `should show the error and a report link to admins`() {
        mockMvc
            .get(ERROR_URL) {
                with(oauth2Login())
                accept = MediaType.TEXT_HTML
                requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500)
                requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/admin/videos")
                requestAttr(RequestDispatcher.ERROR_EXCEPTION, IllegalStateException("Database is down"))
            }.andExpectAll {
                status { isInternalServerError() }
                content { string(containsString("<h1>Something went wrong</h1>")) }
                content { string(containsString("<dd>Database is down</dd>")) }
                content {
                    string(
                        containsString("https://github.com/BSStudio/bss-web-admin-backend/issues/new?template=admin-ui-error.yml"),
                    )
                }
                content { string(containsString("<summary>Stack trace</summary>")) }
            }
    }

    @Test
    internal fun `should explain a missing page without a trace`() {
        mockMvc
            .get(ERROR_URL) {
                with(oauth2Login())
                accept = MediaType.TEXT_HTML
                requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
                requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/admin/nope")
            }.andExpectAll {
                status { isNotFound() }
                content { string(containsString("<h1>We couldn’t find that page</h1>")) }
                content { string(not(containsString("Stack trace"))) }
            }
    }

    @Test
    internal fun `should hide details and reporting from anonymous visitors`() {
        mockMvc
            .get(ERROR_URL) {
                accept = MediaType.TEXT_HTML
                requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 403)
                requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/admin/videos")
            }.andExpectAll {
                status { isForbidden() }
                content { string(containsString("<h1>You can’t do that</h1>")) }
                content { string(not(containsString("Report on GitHub"))) }
            }
    }

    private companion object {
        private const val ERROR_URL = "/error"
    }
}
