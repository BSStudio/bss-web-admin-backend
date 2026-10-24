package hu.bsstudio.bssweb.ui.config

import io.kotest.matchers.shouldBe
import jakarta.servlet.RequestDispatcher
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest

internal class LanguageLinksAdviceTest {
    private val underTest = LanguageLinksAdvice()

    @Test
    internal fun `should keep the search, sort and page and change only the language`() {
        val request =
            MockHttpServletRequest("GET", "/videos").apply {
                queryString = "q=g%C3%B3lya%20t%C3%A1bor&sort=title,asc&lang=en&page=2"
            }

        underTest.languageLinks(request) shouldBe
            mapOf(
                "en" to "/videos?q=g%C3%B3lya%20t%C3%A1bor&sort=title,asc&page=2&lang=en",
                "hu" to "/videos?q=g%C3%B3lya%20t%C3%A1bor&sort=title,asc&page=2&lang=hu",
            )
    }

    @Test
    internal fun `should link a page without a query to itself`() {
        underTest.languageLinks(MockHttpServletRequest("GET", "/"))["hu"] shouldBe "/?lang=hu"
    }

    @Test
    internal fun `should link an error page to the address that failed`() {
        val request =
            MockHttpServletRequest("GET", "/error").apply {
                setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "/videos/42")
                queryString = "lang=en"
            }

        underTest.languageLinks(request)["hu"] shouldBe "/videos/42?lang=hu"
    }
}
