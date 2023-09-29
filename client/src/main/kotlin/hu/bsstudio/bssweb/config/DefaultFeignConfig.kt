package hu.bsstudio.bssweb.config

import feign.auth.BasicAuthRequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DefaultFeignConfig(
    @Value("\${bss.client.username}") val username: String,
    @Value("\${bss.client.password}") val password: String
) {

    @Bean
    fun interceptor() = BasicAuthRequestInterceptor(this.username, this.password)
}
