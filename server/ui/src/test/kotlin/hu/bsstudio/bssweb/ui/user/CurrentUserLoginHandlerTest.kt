package hu.bsstudio.bssweb.ui.user

import hu.bsstudio.bssweb.member.service.MemberService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.ObjectProvider
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User

internal class CurrentUserLoginHandlerTest {
    private val memberService = mockk<MemberService>()
    private val provider = mockk<ObjectProvider<MemberService>> { every { getObject() } returns memberService }

    @Test
    internal fun `should remember the person in the session and go to the dashboard`() {
        every { memberService.findAllMembers() } returns emptyList()
        val user = DefaultOAuth2User(emptyList(), mapOf("sub" to "admin"), "sub")
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()

        CurrentUserLoginHandler(provider).onAuthenticationSuccess(
            request,
            response,
            OAuth2AuthenticationToken(user, emptyList(), "authentik"),
        )

        request.session.getAttribute(CurrentUser.SESSION_KEY) shouldBe CurrentUser("admin", "A", null)
        response.redirectedUrl shouldBe "/"
    }
}
