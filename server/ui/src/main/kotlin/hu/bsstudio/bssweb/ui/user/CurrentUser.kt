package hu.bsstudio.bssweb.ui.user

import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.ui.config.ConditionalOnAdminUi
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import java.io.Serializable
import java.util.UUID

/** The signed-in person as the top bar shows them; [memberId] is set when they are one of the members. */
data class CurrentUser(
    val name: String,
    val initials: String,
    val memberId: UUID?,
) : Serializable {
    companion object {
        /** Where [CurrentUserLoginHandler] keeps the signed-in person for the rest of the session. */
        val SESSION_KEY: String = CurrentUser::class.java.name

        /**
         * The person is a member when their username (`preferred_username`, else the login name) is a member's URL,
         * or else when exactly one member has their full name (`name`).
         */
        fun of(
            user: OAuth2User,
            members: List<Member>,
        ): CurrentUser {
            val name = user.getAttribute<String>("name") ?: user.name
            val username = user.getAttribute<String>("preferred_username") ?: user.name
            val member =
                members.find { it.url.equals(username, ignoreCase = true) }
                    ?: members.singleOrNull { it.name == name }
            return CurrentUser(name, initialsOf(name), member?.id)
        }

        private val SPACES = Regex("\\s+")

        private fun initialsOf(name: String) =
            name
                .trim()
                .split(SPACES)
                .take(2)
                .joinToString("") { it.take(1).uppercase() }
    }
}

/**
 * Puts the signed-in person in every page's model as `currentUser`, as found at login. Sessions from before
 * that lookup existed show the person without a member. API requests sign in with tokens, not as an [OAuth2User],
 * so they get none.
 */
@ConditionalOnAdminUi
@ControllerAdvice
class CurrentUserAdvice {
    @ModelAttribute("currentUser")
    fun currentUser(
        @AuthenticationPrincipal user: OAuth2User?,
        request: HttpServletRequest,
    ): CurrentUser? =
        user?.let {
            // a browser login always has a session; asking for it here keeps token (API) requests sessionless
            request.session.getAttribute(CurrentUser.SESSION_KEY) as CurrentUser? ?: CurrentUser.of(it, emptyList())
        }
}
