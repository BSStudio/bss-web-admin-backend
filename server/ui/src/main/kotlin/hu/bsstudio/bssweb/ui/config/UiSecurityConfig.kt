package hu.bsstudio.bssweb.ui.config

import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.ui.user.CurrentUserLoginHandler
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint

/**
 * Browser login for the admin UI. Runs before the API's token-based filter chain
 * and only takes over the admin pages, the OAuth2 login endpoints and the error page.
 * The UI stays disabled until the `authentik` client registration is configured.
 */
@Configuration
@ConditionalOnAdminUi
class UiSecurityConfig {
    @Bean
    @Order(1)
    fun uiSecurityFilterChain(
        http: HttpSecurity,
        clientRegistrationRepository: ClientRegistrationRepository,
        memberService: ObjectProvider<MemberService>,
    ): SecurityFilterChain {
        http {
            // /error is here so the admin's error page renders after a session has expired. Every error of the app,
            // the API's too, is rendered in an /error dispatch, so it passes this chain with permitAll; API errors
            // still get Spring's plain JSON body, because AdminErrorAttributes adds details only for /admin pages.
            securityMatcher("/admin/**", "/oauth2/**", "/login/oauth2/**", "/error")
            authorizeHttpRequests {
                authorize("/admin/css/**", permitAll)
                authorize("/admin/js/**", permitAll)
                authorize("/admin/icons/**", permitAll)
                authorize("/error", permitAll)
                authorize(anyRequest, authenticated)
            }
            oauth2Login {
                authenticationSuccessHandler = CurrentUserLoginHandler(memberService)
            }
            headers {
                // the templates have no inline script, style or event handler, so everything can come from the
                // app itself; form-action is left out because logging out redirects to Authentik
                contentSecurityPolicy { policyDirectives = CONTENT_SECURITY_POLICY }
            }
            exceptionHandling {
                authenticationEntryPoint = loginEntryPoint()
            }
            logout {
                logoutUrl = "/admin/logout"
                logoutSuccessHandler =
                    OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository).apply {
                        // used when the provider has an end-session endpoint (Authentik does)
                        setPostLogoutRedirectUri("{baseUrl}/admin")
                        // used otherwise
                        setDefaultTargetUrl("/admin")
                    }
            }
        }

        return http.build()
    }

    private fun loginEntryPoint(): AuthenticationEntryPoint {
        val login = LoginUrlAuthenticationEntryPoint("/oauth2/authorization/authentik")
        return AuthenticationEntryPoint { request, response, exception ->
            if (request.getHeader("HX-Request") == null) {
                login.commence(request, response, exception)
            } else {
                // htmx can't follow the login redirect; it reloads the page instead, which then logs in normally
                response.setHeader("HX-Refresh", "true")
                response.status = HttpServletResponse.SC_UNAUTHORIZED
            }
        }
    }

    private companion object {
        private const val CONTENT_SECURITY_POLICY =
            "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; object-src 'none'; " +
                "base-uri 'self'; frame-ancestors 'none'"
    }
}
