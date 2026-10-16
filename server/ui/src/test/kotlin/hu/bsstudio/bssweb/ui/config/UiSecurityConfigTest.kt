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
        mockMvc.get("/admin").andExpect {
            status { is3xxRedirection() }
            redirectedUrl("/oauth2/authorization/authentik")
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
                        startsWith("http://localhost:8999/default/authorize?response_type=code"),
                        containsString("redirect_uri=http://localhost/login/oauth2/code/authentik"),
                    ),
                )
            }
        }
    }

    @Test
    internal fun `should serve stylesheets and webjars without login`() {
        listOf(
            "/admin/css/admin.css",
            "/admin/webjars/picocss__pico/css/pico.min.css",
            "/admin/webjars/htmx.org/dist/htmx.min.js",
        ).forEach { url ->
            mockMvc.get(url).andExpect { status { isOk() } }
        }
    }

    @Test
    internal fun `should reject logout without a CSRF token`() {
        mockMvc
            .post("/admin/logout") {
                with(oauth2Login())
            }.andExpect {
                status { isForbidden() }
            }
    }

    @Test
    internal fun `should log out and return to the admin`() {
        mockMvc
            .post("/admin/logout") {
                with(oauth2Login())
                with(csrf())
            }.andExpect {
                status { is3xxRedirection() }
                redirectedUrl("/admin")
            }
    }
}
