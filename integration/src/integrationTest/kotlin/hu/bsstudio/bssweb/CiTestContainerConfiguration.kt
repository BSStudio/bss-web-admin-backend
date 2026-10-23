package hu.bsstudio.bssweb

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File

@Profile("ci")
@TestConfiguration(proxyBeanMethods = false)
class CiTestContainerConfiguration(
    @Value("\${integration.testcontainers.rebuild-images:false}") private val rebuildImages: Boolean,
) {
    @Bean
    fun dockerComposeContainer(): ComposeContainer =
        ComposeContainer(
            File("../docker-compose.yml"),
            File("../docker-compose.ci.yml"),
        ).withExposedService("postgres", 5432, Wait.forHealthcheck())
            .withExposedService("app", 8080, Wait.forHealthcheck())
            // not exposed to the tests, but the app needs them ready (same as `docker compose up --wait`)
            .waitingFor("mock-file-api", Wait.forHealthcheck())
            .waitingFor("mock-oidc", Wait.forHealthcheck())
            // the stack is torn down after the run, keep the app logs in the test output
            .withLogConsumer("app", Slf4jLogConsumer(LoggerFactory.getLogger("compose.app")))
            .withEnv("COMPOSE_PROFILES", "app")
            .withBuild(rebuildImages)

    @Bean
    fun ciIntegrationProperties(compose: ComposeContainer): DynamicPropertyRegistrar =
        DynamicPropertyRegistrar { registry ->
            registry.add("spring.datasource.url") {
                "jdbc:postgresql://${compose.getServiceHost("postgres", 5432)}:" +
                    "${compose.getServicePort("postgres", 5432)}/bss?currentSchema=private"
            }
            registry.add("spring.datasource.username") { "user" }
            registry.add("spring.datasource.password") { "password" }
            registry.add("bss.client.url") {
                "http://${compose.getServiceHost("app", 8080)}:${compose.getServicePort("app", 8080)}"
            }
        }
}
