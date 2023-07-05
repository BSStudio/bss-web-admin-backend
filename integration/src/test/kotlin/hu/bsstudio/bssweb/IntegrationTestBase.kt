package hu.bsstudio.bssweb

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File


@SpringBootTest(
        classes = [BssWebApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ComponentScan
@Testcontainers
abstract class IntegrationTestBase {
    companion object {

        @Container
        private val compose = DockerComposeContainer(
                File("./../docker-compose.yml"),
                File("./../docker-compose.ci.yml")
        )
                .withServices("db", "influx", "mock-file-api")
                .withExposedService("db", 5432)
                .withExposedService("influx", 8086)
                .withExposedService("mock-file-api", 8080)


        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("bss.file-api.url") { "localhost" }
            registry.add("bss.file-api.url") { "http://${composeHostPort("mock-file-api", 8080)}" }
            registry.add("spring.security.user.password") { "password" }
            registry.add("spring.datasource.url") { "jdbc:postgresql://${composeHostPort("db", 5432)}/postgres?currentSchema=bss_web" }
            registry.add("spring.datasource.username") { "postgres" }
            registry.add("spring.datasource.password") { "postgres" }
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
            registry.add("management.influx.metrics.export.uri") { "http://${composeHostPort("influx", 8086)}" }
            registry.add("management.influx.metrics.export.token") { "token" }
            registry.add("management.influx.metrics.export.org") { "org" }
            registry.add("management.influx.metrics.export.user-name") { "user" }
            registry.add("management.influx.metrics.export.password") { "password" }
            registry.add("management.influx.metrics.export.bucket") { "bucket" }
        }

        private fun composeHostPort(serviceName: String, servicePort: Int) = "${compose.getServiceHost(serviceName, servicePort)}:${compose.getServicePort(serviceName, servicePort)}"
    }
}
