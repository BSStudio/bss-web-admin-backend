package hu.bsstudio.bssweb.security.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration

internal class SecurityConfigTest {
    private val contextRunner = ApplicationContextRunner().withUserConfiguration(Probe::class.java)

    @Test
    internal fun `should match without a client registration`() {
        contextRunner.run { assertThat(it).hasBean("probe") }
    }

    @Test
    internal fun `should not match with a client registration`() {
        contextRunner
            .withPropertyValues("spring.security.oauth2.client.registration.authentik.client-id=admin-ui")
            .run { assertThat(it).doesNotHaveBean("probe") }
    }

    @Configuration
    class Probe {
        @Bean
        @Conditional(SecurityConfig.WithoutAdminUi::class)
        fun probe() = "probe"
    }
}
