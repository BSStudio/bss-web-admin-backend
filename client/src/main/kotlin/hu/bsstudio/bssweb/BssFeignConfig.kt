package hu.bsstudio.bssweb

import feign.auth.BasicAuthRequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableFeignClients("hu.bsstudio.bssweb.**.client", defaultConfiguration = [BssFeignConfig.DefaultFeignConfig::class])
@ImportAutoConfiguration(FeignAutoConfiguration::class, HttpMessageConvertersAutoConfiguration::class)
class BssFeignConfig {

    @Configuration
    class DefaultFeignConfig {

        @Bean
        fun interceptor(
            @Value("\${hu.bsstudio.bssweb.client.username:user}") username: String,
            @Value("\${hu.bsstudio.bssweb.client.password:password}") password: String
        ) = BasicAuthRequestInterceptor(username, password)
    }
}
