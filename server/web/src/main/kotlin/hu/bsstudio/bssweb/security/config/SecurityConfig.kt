package hu.bsstudio.bssweb.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
            }
            .oauth2ResourceServer{
                it.opaqueToken {
                    // load settings from Spring Boot
                    // spring.security.oauth2.resourceserver.opaquetoken
                }
            }
            .cors { it.disable() }
            .csrf { it.disable() }
            .build()
    }
}
