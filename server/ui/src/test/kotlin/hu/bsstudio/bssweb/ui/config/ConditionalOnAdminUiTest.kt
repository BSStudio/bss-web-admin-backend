package hu.bsstudio.bssweb.ui.config

import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldHaveSize
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Configuration

internal class ConditionalOnAdminUiTest {
    private val runner = ApplicationContextRunner().withUserConfiguration(AdminUiBean::class.java)

    @Test
    internal fun `should leave the UI out without the login's client id`() {
        runner.run { it.getBeansOfType(AdminUiBean::class.java).shouldBeEmpty() }
    }

    @Test
    internal fun `should turn the UI on with the login's client id`() {
        runner
            .withPropertyValues("spring.security.oauth2.client.registration.authentik.client-id=admin-ui")
            .run { it.getBeansOfType(AdminUiBean::class.java) shouldHaveSize 1 }
    }

    @Configuration
    @ConditionalOnAdminUi
    internal class AdminUiBean
}
