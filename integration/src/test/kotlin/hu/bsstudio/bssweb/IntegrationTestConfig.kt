package hu.bsstudio.bssweb

import feign.auth.BasicAuthRequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.test.context.TestPropertySource

@Configuration
@EnableFeignClients
@Profile("local")
@TestPropertySource(properties = [
    "client.username=user",
    "client.password=password",
    "spring.cloud.openfeign.client.config.bss-web.url=http://localhost:8080",
    "spring.cloud.openfeign.client.config.bss-web.request-interceptors[0]=bssClientInterceptor",
])
class IntegrationTestConfig {

    @Value("\${client.username}")
    private lateinit var username: String

    @Value("\${client.password}")
    private lateinit var password: String

    @Bean
    fun bssClientInterceptor() = BasicAuthRequestInterceptor(username, password)
}
