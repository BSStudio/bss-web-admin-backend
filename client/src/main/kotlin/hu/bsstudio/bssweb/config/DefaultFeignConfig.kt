package hu.bsstudio.bssweb.config

import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DefaultFeignConfig {
    @Bean
    fun interceptor() =
        RequestInterceptor { template ->
            template.header("Authorization", "Bearer token")
        }
}
