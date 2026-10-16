package hu.bsstudio.bssweb.dashboard.page

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.metrics.service.MetricsService
import hu.bsstudio.bssweb.ui.config.UiSecurityConfig
import io.mockk.every
import jakarta.servlet.http.Cookie
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(DashboardPageController::class)
@Import(UiSecurityConfig::class)
internal class DashboardPageControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var service: MetricsService

    @BeforeEach
    internal fun setUp() {
        every { service.getMetrics() } returns METRICS
    }

    @Test
    internal fun `should render the dashboard in English by default`() {
        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/dashboard") }
                model { attribute("metrics", METRICS) }
                content { string(containsString("""<html lang="en">""")) }
                content { string(containsString("<h1>Dashboard</h1>")) }
                content { string(containsString("""<p class="metric">42</p>""")) }
            }
    }

    @Test
    internal fun `should switch to Hungarian and remember it in a cookie`() {
        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("lang", "hu")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<html lang="hu">""")) }
                content { string(containsString("<h1>Áttekintés</h1>")) }
                cookie { value(LOCALE_COOKIE, "hu") }
            }
    }

    @Test
    internal fun `should render in Hungarian when the cookie asks for it`() {
        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                cookie(Cookie(LOCALE_COOKIE, "hu"))
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("<h1>Áttekintés</h1>")) }
                content { string(containsString(">English</a>")) }
            }
    }

    @Test
    internal fun `should fall back to English for an unsupported language`() {
        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("lang", "de")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("<h1>Dashboard</h1>")) }
            }
    }

    private companion object {
        private const val BASE_URL = "/admin"
        private const val LOCALE_COOKIE = "bss-admin-lang"
        private val METRICS = BssMetrics(videoCount = 42, eventCount = 7, memberCount = 120)
    }
}
