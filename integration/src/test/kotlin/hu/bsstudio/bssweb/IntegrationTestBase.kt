package hu.bsstudio.bssweb

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
abstract class IntegrationTestBase {
    companion object {

        private val COMPOSE_FILE = File("./../docker-compose.yml")

        private val COMPOSE_FILE_CI = File("./../docker-compose.ci.yml")

        @Container
        private val compose = DockerComposeContainer(COMPOSE_FILE, COMPOSE_FILE_CI)
            .withExposedService("app", 8080)
            .withExposedService("db", 5432)
            .withExposedService("influx", 8086)
            .withExposedService("mock-file-api", 8080)


        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            val dbHost = compose.getServiceHost("db", 5432)
            val dbPort = compose.getServicePort("db", 5432)
            registry.add("spring.datasource.url") { "jdbc:postgresql://$dbHost:$dbPort/postgres?currentSchema=bss_web" }
            registry.add("spring.datasource.username") { "postgres" }
            registry.add("spring.datasource.password") { "postgres" }
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
        }
    }
}
