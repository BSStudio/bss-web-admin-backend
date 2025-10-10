package hu.bsstudio.bssweb

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

@TestConfiguration
class TestContainerConfiguration {

    @Bean
    fun dockerComposeContainer(): DockerComposeContainer<*> {
        return DockerComposeContainer(File("../docker-compose.yml"), File("../docker-compose.ci.yml"))
            .withExposedService("postgres", 5432)
            .withExposedService("app", 8080)
            .withOptions("--profile app")
            .withBuild(true)
    }

    @DynamicPropertySource
    fun containerPropertyConfigurer(registry: DynamicPropertyRegistry, dockerComposeContainer: DockerComposeContainer<*>) {
        val postgresHost = dockerComposeContainer.getServiceHost("postgres", 5432)
        val postgresPort = dockerComposeContainer.getServicePort("postgres", 5432)
        val appHost = dockerComposeContainer.getServiceHost("app", 8080)
        val appPort = dockerComposeContainer.getServicePort("app", 8080)

        registry.add("spring.datasource.url") {
            "jdbc:postgresql://$postgresHost:$postgresPort/bss?currentSchema=private"
        }
        registry.add("spring.datasource.username") { "user" }
        registry.add("spring.datasource.password") { "password" }
        registry.add("spring.flyway.enabled") { "false" }
        registry.add("bss.client.url") { "http://$appHost:$appPort" }
    }
}
