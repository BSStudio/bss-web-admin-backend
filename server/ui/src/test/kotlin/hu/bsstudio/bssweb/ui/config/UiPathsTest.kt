package hu.bsstudio.bssweb.ui.config

import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest

internal class UiPathsTest {
    @Test
    internal fun `should count the pages, their assets and the login as the UI's`() {
        listOf("/", "/videos", "/videos/42", "/css/admin.css", "/logout", "/oauth2/authorization/authentik", "/error")
            .forAll { UiPaths.isUi(it) shouldBe true }
    }

    @Test
    internal fun `should leave the API, its documentation and the actuator alone`() {
        listOf(
            "/api/v1/video",
            "/actuator/health",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/v3/api-docs/swagger-config",
            "/open-api.yaml",
            "/webjars/htmx.org/dist/htmx.min.js",
        ).forAll { UiPaths.isUi(it) shouldBe false }
    }

    @Test
    internal fun `should read a request's address without the context path`() {
        val request = MockHttpServletRequest("GET", "/bss/api/v1/video").apply { contextPath = "/bss" }

        UiPaths.isUi(request) shouldBe false
    }
}
