package hu.bsstudio.bssweb.security.config

import hu.bsstudio.bssweb.member.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun bssAuthenticationProvider() : BssAuthenticationProvider {
        return BssAuthenticationProvider()
    }

    @Bean
    fun authManager(http: HttpSecurity, bssAuthenticationProvider: BssAuthenticationProvider): AuthenticationManager? {
        return http
                .getSharedObject(AuthenticationManagerBuilder::class.java)
                .authenticationProvider(bssAuthenticationProvider)
                .build()
    }


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
