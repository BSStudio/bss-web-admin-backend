package hu.bsstudio.bssweb.security.config

import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions
import org.springframework.boot.health.actuate.endpoint.HealthEndpoint
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest
import org.springframework.boot.security.oauth2.client.autoconfigure.ConditionalOnOAuth2ClientRegistrationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher

/**
 * Token-based security for the REST API, its documentation and the actuator. The admin UI's chain, when there is
 * one, takes every other address; without it, [fallbackSecurityFilterChain] asks for a token there too.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    @Order(1)
    fun apiSecurityFilterChain(http: HttpSecurity): SecurityFilterChain =
        tokenAuthentication(http.securityMatcher(OrRequestMatcher(API_PATHS.map(path::matcher) + EndpointRequest.toAnyEndpoint())))

    @Bean
    @Conditional(WithoutAdminUi::class)
    fun fallbackSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = tokenAuthentication(http)

    private fun tokenAuthentication(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(EndpointRequest.to(HealthEndpoint::class.java, InfoEndpoint::class.java), permitAll)
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer { opaqueToken { } }
            // only the token counts: the admin UI's session cookie doesn't log in to the API
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
        }
        return http.build()
    }

    private companion object {
        private val path = PathPatternRequestMatcher.withDefaults()
        private val API_PATHS = listOf("/api/**", "/open-api.yaml", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
    }

    /** Matches when no OAuth2 client registration is configured, which is when the admin UI is turned off. */
    internal class WithoutAdminUi : NoneNestedConditions(ConfigurationPhase.REGISTER_BEAN) {
        @ConditionalOnOAuth2ClientRegistrationProperties
        class ClientRegistration
    }
}
