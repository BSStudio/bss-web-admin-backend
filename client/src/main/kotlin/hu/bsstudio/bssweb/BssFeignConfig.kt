package hu.bsstudio.bssweb

import feign.auth.BasicAuthRequestInterceptor
import org.bouncycastle.cms.RecipientId.password
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
    class DefaultFeignConfig(
        @Value("\${bss.client.username}") val username: String,
        @Value("\${bss.client.password}") val password: String
    ) {

        @Bean
        fun interceptor() = BasicAuthRequestInterceptor(this.username, this.password)
    }
}
