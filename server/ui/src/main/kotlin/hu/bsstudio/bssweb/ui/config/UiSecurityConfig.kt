package hu.bsstudio.bssweb.ui.config

import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.ui.user.CurrentUserLoginHandler
import io.github.wimdeblauwe.htmx.spring.boot.security.HxRedirectToPageAuthenticationEntryPoint
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher

/**
 * Browser login for the admin UI. Runs before the API's token-based filter chain and takes over every address
 * that [UiPaths] doesn't leave to the API: the admin pages, the OAuth2 login endpoints and the error page.
 * The UI stays disabled until a client registration is configured, and logs in with it
 * whatever its id is: `authentik` in production.
 */
@Configuration
@ConditionalOnAdminUi
class UiSecurityConfig {
    @Bean
    @Order(1)
    fun uiSecurityFilterChain(
        http: HttpSecurity,
        oauth2Client: OAuth2ClientProperties,
        clientRegistrationRepository: ClientRegistrationRepository,
        memberService: ObjectProvider<MemberService>,
    ): SecurityFilterChain {
        val login = LoginUrlAuthenticationEntryPoint("/oauth2/authorization/${oauth2Client.registration.keys.single()}")
        http {
            // Every error of the app, the API's too, is rendered in an /error dispatch, so it passes this chain with
            // permitAll; API errors still get Spring's plain JSON body, because AdminErrorAttributes adds details
            // only for the UI's pages.
            securityMatcher(UI_ADDRESSES)
            authorizeHttpRequests {
                authorize("/css/**", permitAll)
                authorize("/js/**", permitAll)
                authorize("/icons/**", permitAll)
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
                // htmx can't follow the login redirect; it loads the page it was on instead, which then logs in normally
                authenticationEntryPoint = HxRedirectToPageAuthenticationEntryPoint(login)
            }
            logout {
                logoutUrl = "/logout"
                logoutSuccessHandler =
                    OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository).apply {
                        // used when the provider has an end-session endpoint (Authentik does)
                        setPostLogoutRedirectUri("{baseUrl}/")
                        // used otherwise
                        setDefaultTargetUrl("/")
                    }
            }
        }

        return http.build()
    }

    private companion object {
        private val UI_ADDRESSES =
            NegatedRequestMatcher(OrRequestMatcher(UiPaths.EXCLUDED.map(PathPatternRequestMatcher.withDefaults()::matcher)))

        private const val CONTENT_SECURITY_POLICY =
            "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; object-src 'none'; " +
                "base-uri 'self'; frame-ancestors 'none'"
    }
}
