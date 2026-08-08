package hu.bsstudio.bssweb

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.containers.ComposeContainer
import java.io.File

@Profile("ci")
@TestConfiguration(proxyBeanMethods = false)
class CiTestContainerConfiguration {
    @Bean
    fun dockerComposeContainer(): ComposeContainer =
        ComposeContainer(
            File("../docker-compose.yml"),
            File("../docker-compose.ci.yml"),
        ).withExposedService("postgres", 5432)
            .withExposedService("app", 8080)
            .withEnv("COMPOSE_PROFILES", "app")
            .withBuild(System.getenv("REBUILD_IMAGES")?.toBoolean() ?: false)

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
