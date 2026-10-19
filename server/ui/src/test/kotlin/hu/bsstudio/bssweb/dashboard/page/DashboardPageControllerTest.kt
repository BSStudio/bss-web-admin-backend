package hu.bsstudio.bssweb.dashboard.page

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.metrics.service.MetricsService
import hu.bsstudio.bssweb.ui.config.UiSecurityConfig
import hu.bsstudio.bssweb.ui.flash.Flash
import hu.bsstudio.bssweb.ui.user.CurrentUser
import io.mockk.every
import jakarta.servlet.http.Cookie
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID

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
                content { string(containsString("""class="cds--header__name brand" title="Dashboard"""")) }
                content { string(containsString("""aria-current="page"><strong class="badge">BSS</strong>""")) }
                content { string(containsString("""<span class="metric">42</span>""")) }
                content { string(containsString("""<span class="metric">9</span>""")) }
                content { string(containsString("""<h2 id="guide-title">How the admin works</h2>""")) }
                content { string(containsString("""<a class="cds--skip-to-content skip-link" href="#main">Skip to content</a>""")) }
                content { string(containsString("""<main id="main" class="container">""")) }
                content { string(containsString("""<meta name="htmx-config"""")) }
                content { string(containsString("""hx-headers:inherited='{&quot;X-CSRF-TOKEN&quot;: """)) }
                content { string(containsString("""hx-status:5xx:inherited="target:body swap:innerHTML select:body push:false"""")) }
                content {
                    string(
                        containsString("""<button type="button" class="cds--header__action menu-toggle" popovertarget="sections">"""),
                    )
                }
                content { string(containsString("""<ul id="sections" class="cds--header__menu-bar sections" popover>""")) }
                content { string(containsString("""<span class="cds--header__action avatar"><span class="initials">U</span></span>""")) }
                content { string(containsString("""<a href="/admin?lang=en" hreflang="en" lang="en" title="English"""")) }
                content { string(containsString("""<a href="/admin?lang=hu" hreflang="hu" lang="hu" title="Magyar"""")) }
                content { string(containsString("""aria-current="true">EN</a>""")) }
                content { string(containsString("""<button type="submit" class="cds--btn cds--btn--ghost">Log out</button>""")) }
            }
    }

    @Test
    internal fun `should link the avatar to the signed-in member's page`() {
        val me = CurrentUser("Kovács Anna", "KA", UUID.randomUUID())

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                sessionAttr(CurrentUser.SESSION_KEY, me)
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""class="cds--header__action avatar" href="/admin/members/${me.memberId}"""")) }
                content { string(containsString("""<span class="initials">KA</span>""")) }
                content { string(containsString("""<p class="who">Kovács Anna</p>""")) }
            }
    }

    @Test
    internal fun `should redirect an address with a trailing slash to the one without it`() {
        mockMvc
            .get("$BASE_URL/?lang=hu") {
                with(oauth2Login())
            }.andExpectAll {
                status { isPermanentRedirect() }
                redirectedUrl("$BASE_URL?lang=hu")
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
                content { string(containsString("""aria-current="true">HU</a>""")) }
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
                content { string(containsString("""<section id="toasts" class="toasts" role="status" aria-label="Notifications">""")) }
                content { string(containsString("""<div class="cds--toast-notification__title">Saved “Gólyatábor 2026 - Vám”.</div>""")) }
                content { string(not(containsString("role=\"alert\""))) }
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
                content { string(containsString("""<div role="alert"""")) }
                content {
                    string(
                        containsString("""<div class="cds--inline-notification__subtitle">Select at least one video first.</div>"""),
                    )
                }
                content { string(not(containsString("cds--toast-notification__title"))) }
            }
    }

    private companion object {
        private const val BASE_URL = "/admin"
        private const val LOCALE_COOKIE = "bss-admin-lang"
        private val METRICS = BssMetrics(videoCount = 42, eventCount = 7, memberCount = 120, labelCount = 9)
    }
}
