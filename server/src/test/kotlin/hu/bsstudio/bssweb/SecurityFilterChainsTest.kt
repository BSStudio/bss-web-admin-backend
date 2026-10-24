package hu.bsstudio.bssweb

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.mock.web.MockHttpSession
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class SecurityFilterChainsTest(
    @param:Autowired private val mockMvc: MockMvc,
) {
    @Test
    fun `should ask for a token on the API`() {
        mockMvc.get("/api/v1/video").andExpect { status { isUnauthorized() } }
    }

    @Test
    fun `should serve the API's documentation without a token`() {
        mockMvc.get("/swagger-ui.html").andExpect { redirectedUrl("/swagger-ui/index.html") }
        listOf("/swagger-ui/index.html", "/open-api.yaml", "/v3/api-docs/swagger-config").forEach { url ->
            mockMvc.get(url).andExpect { status { isOk() } }
        }
    }

    @Test
    fun `should not let the admin's session log in to the API`() {
        val admin = OAuth2AuthenticationToken(DefaultOAuth2User(emptyList(), mapOf("sub" to "jdoe"), "sub"), emptyList(), "authentik")
        val session =
            MockHttpSession().apply {
                setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextImpl(admin))
            }

        mockMvc.get("/videos") { this.session = session }.andExpect { status { isOk() } }
        mockMvc.get("/api/v1/video") { this.session = session }.andExpect { status { isUnauthorized() } }
    }

    @Test
    fun `should leave the health check open`() {
        mockMvc.get("/actuator/health").andExpect { status { isOk() } }
    }

    @Test
    fun `should send a browser to the admin's login`() {
        mockMvc.get("/videos").andExpect { redirectedUrl("/oauth2/authorization/authentik") }
    }

    @Test
    fun `should serve the admin's assets without a login`() {
        listOf("/css/admin.css", "/icons/favicon.svg", "/webjars/htmx.org/dist/htmx.min.js").forEach { url ->
            mockMvc.get(url).andExpect { status { isOk() } }
        }
    }
}
