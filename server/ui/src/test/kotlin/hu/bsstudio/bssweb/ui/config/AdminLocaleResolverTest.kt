package hu.bsstudio.bssweb.ui.config

import io.kotest.matchers.shouldBe
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import java.util.Locale

internal class AdminLocaleResolverTest {
    private val underTest = AdminLocaleResolver(COOKIE)

    @Test
    internal fun `should read the admin's language from the cookie, English by default`() {
        underTest.resolveLocale(request("/admin/videos")) shouldBe Locale.ENGLISH
        underTest.resolveLocale(request("/admin/videos").apply { setCookies(Cookie(COOKIE, "hu")) }) shouldBe HUNGARIAN
    }

    @Test
    internal fun `should remember a language the admin chooses in the cookie`() {
        val response = MockHttpServletResponse()

        underTest.setLocale(request("/admin"), response, HUNGARIAN)

        response.getCookie(COOKIE)?.value shouldBe "hu"
    }

    @Test
    internal fun `should treat an admin page's error page as part of the admin`() {
        val request =
            request("/error").apply {
                setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "/admin/videos")
                setCookies(Cookie(COOKIE, "hu"))
            }

        underTest.resolveLocale(request) shouldBe HUNGARIAN
    }

    @Test
    internal fun `should leave the API to Accept-Language`() {
        val request =
            request("/api/v1/video").apply {
                setCookies(Cookie(COOKIE, "en"))
                addHeader("Accept-Language", "hu")
            }

        underTest.resolveLocale(request) shouldBe HUNGARIAN
    }

    private fun request(uri: String) = MockHttpServletRequest("GET", uri)

    private companion object {
        private const val COOKIE = "bss-admin-lang"
        private val HUNGARIAN = Locale.forLanguageTag("hu")
    }
}
