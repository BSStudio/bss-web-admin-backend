package hu.bsstudio.bssweb

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

@SpringJUnitConfig(classes = [BssFeignConfig::class])
@TestPropertySource(
    properties = [
        "bss.client.username=user",
        "bss.client.password=password"
    ]
)
@Testcontainers
open class IntegrationTest {

    companion object {
        @JvmStatic
        @Container
        private val container = DockerComposeContainer(
            File("../docker-compose.yml"),
            File("../docker-compose.ci.yml")
        )

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("bss.client.url") {
                val host = container.getServiceHost("app_1", 8080)
                val port = container.getServicePort("app_1", 8080)
                "http://$host:$port"
            }
        }
    }
}
