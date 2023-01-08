package hu.bsstudio.bssweb.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
                .authorizeHttpRequests {
                    it.requestMatchers("/api/**").authenticated()
                      .anyRequest().permitAll()
                }
                .httpBasic(Customizer.withDefaults())
                .cors().disable()
                .csrf().disable()
                .build()
    }
}
