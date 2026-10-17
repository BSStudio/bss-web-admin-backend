package hu.bsstudio.bssweb.ui.user

import hu.bsstudio.bssweb.member.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.ObjectProvider
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler

/**
 * After a browser login, finds out whether the person is one of the members ([CurrentUser.of]) and keeps the answer
 * in the session, so the members are read once per login rather than on every page. Then it continues to the page
 * that asked for the login, or to the dashboard.
 */
class CurrentUserLoginHandler(
    private val memberService: ObjectProvider<MemberService>,
) : SavedRequestAwareAuthenticationSuccessHandler() {
    init {
        defaultTargetUrl = "/admin"
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val user = authentication.principal as OAuth2User
        val members = memberService.getObject().findAllMembers()
        request.session.setAttribute(CurrentUser.SESSION_KEY, CurrentUser.of(user, members))
        super.onAuthenticationSuccess(request, response, authentication)
    }
}
