package hu.bsstudio.bssweb

import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource

@Configuration
@TestPropertySource(
    properties = [
        "hu.bsstudio.bssweb.client.username=user",
        "hu.bsstudio.bssweb.client.password=password",
        "spring.cloud.openfeign.client.config.member.url=http://localhost:9999"
    ]
)
class IntegrationTestConfig {
    constructor() {
        println("IntegrationTestConfig")
    }
}
