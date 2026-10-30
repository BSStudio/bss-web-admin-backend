package hu.bsstudio.bssweb.config

import io.kotest.matchers.collections.shouldContainExactly
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.junit.jupiter.api.Test

internal class FlywayConfigTest {
    @Test
    internal fun `should read the migrations from pgsql files`() {
        val configuration = FluentConfiguration()

        FlywayConfig().pgsqlMigrations().customize(configuration)

        configuration.sqlMigrationSuffixes.toList() shouldContainExactly listOf(".pgsql")
    }
}
