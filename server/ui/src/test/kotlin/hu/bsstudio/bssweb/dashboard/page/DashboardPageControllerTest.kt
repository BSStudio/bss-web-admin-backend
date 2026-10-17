package hu.bsstudio.bssweb.dashboard.page

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.metrics.service.MetricsService
import hu.bsstudio.bssweb.ui.config.UiSecurityConfig
import hu.bsstudio.bssweb.ui.flash.Flash
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
                content { string(containsString("""<span class="metric">42</span>""")) }
                content { string(containsString("""<a class="skip-link" href="#main">Skip to content</a>""")) }
                content { string(containsString("""<main id="main" class="container">""")) }
                content { string(containsString("""<meta name="htmx-config"""")) }
                content { string(containsString("&quot;Accept&quot;: &quot;text/html&quot;")) }
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

    @Test
    internal fun `should show a message carried over from the previous page`() {
        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                flashAttr("flash", Flash("video.flash.saved", "Gólyatábor 2026 - Vám"))
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<p class="flash" role="status">Saved “Gólyatábor 2026 - Vám”.</p>""")) }
            }
    }

    @Test
    internal fun `should show an error message as an alert`() {
        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                flashAttr("flash", Flash("video.flash.noneSelected", error = true))
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<p class="flash error" role="alert">Select at least one video first.</p>""")) }
            }
    }

    private companion object {
        private const val BASE_URL = "/admin"
        private const val LOCALE_COOKIE = "bss-admin-lang"
        private val METRICS = BssMetrics(videoCount = 42, eventCount = 7, memberCount = 120)
    }
}
