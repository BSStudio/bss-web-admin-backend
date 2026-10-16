package hu.bsstudio.bssweb.ui.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain

/**
 * Browser login for the admin UI. Runs before the API's token-based filter chain
 * and only takes over the admin pages and the OAuth2 login endpoints.
 * The UI stays disabled until the `authentik` client registration is configured.
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.security.oauth2.client.registration.authentik", name = ["client-id"])
class UiSecurityConfig {
    @Bean
    @Order(1)
    fun uiSecurityFilterChain(
        http: HttpSecurity,
        clientRegistrationRepository: ClientRegistrationRepository,
    ): SecurityFilterChain {
        http {
            securityMatcher("/admin/**", "/oauth2/**", "/login/oauth2/**")
            authorizeHttpRequests {
                authorize("/admin/css/**", permitAll)
                authorize("/admin/webjars/**", permitAll)
                authorize(anyRequest, authenticated)
            }
            oauth2Login { }
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
}
