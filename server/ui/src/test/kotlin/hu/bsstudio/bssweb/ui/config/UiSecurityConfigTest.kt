package hu.bsstudio.bssweb.ui.config

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.dashboard.page.DashboardPageController
import hu.bsstudio.bssweb.metrics.service.MetricsService
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(DashboardPageController::class)
@Import(UiSecurityConfig::class)
internal class UiSecurityConfigTest(
    @param:Autowired private val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var service: MetricsService

    @Test
    internal fun `should send anonymous users to the identity provider`() {
        mockMvc.get("/").andExpect {
            status { is3xxRedirection() }
            redirectedUrl("/oauth2/authorization/authentik")
        }
    }

    @Test
    internal fun `should ask htmx to reload the page when the session has expired`() {
        mockMvc
            .get("/videos") {
                header("HX-Request", "true")
            }.andExpectAll {
                status { isUnauthorized() }
                header { string("HX-Refresh", "true") }
            }
    }

    @Test
    internal fun `should send htmx back to its page when the session has expired`() {
        mockMvc
            .get("/videos") {
                header("HX-Request", "true")
                header("HX-Current-URL", "http://localhost/videos?page=2")
            }.andExpectAll {
                status { isUnauthorized() }
                header { string("HX-Redirect", "/videos?page=2") }
            }
    }

    @Test
    internal fun `should start the authorization code flow`() {
        mockMvc.get("/oauth2/authorization/authentik").andExpect {
            status { is3xxRedirection() }
            header {
                string(
                    "Location",
                    allOf(
                        startsWith("http://localhost:8998/login/authorize?response_type=code"),
                        containsString("redirect_uri=http://localhost/login/oauth2/code/authentik"),
                    ),
                )
            }
        }
    }

    @Test
    internal fun `should serve stylesheets and webjars without login`() {
        listOf(
            "/css/admin.css",
            "/js/admin.js",
            "/webjars/htmx.org/dist/htmx.min.js",
        ).forEach { url ->
            mockMvc.get(url).andExpect { status { isOk() } }
        }
    }

    @Test
    internal fun `should reject logout without a CSRF token`() {
        mockMvc
            .post("/logout") {
                with(oauth2Login())
            }.andExpect {
                status { isForbidden() }
            }
    }

    @Test
    internal fun `should log out and return to the admin`() {
        mockMvc
            .post("/logout") {
                with(oauth2Login())
                with(csrf())
            }.andExpect {
                status { is3xxRedirection() }
                redirectedUrl("/")
            }
    }
}
