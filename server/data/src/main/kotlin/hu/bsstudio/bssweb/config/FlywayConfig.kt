package hu.bsstudio.bssweb.config

import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Makes Flyway read the migrations from `.pgsql` files instead of `.sql`. SonarCloud analyses every `.sql` file as
 * Oracle PL/SQL, which has no rules for PostgreSQL; Squawk and SQLFluff lint the migrations instead.
 */
@Configuration
class FlywayConfig {
    @Bean
    fun pgsqlMigrations() = FlywayConfigurationCustomizer { it.sqlMigrationSuffixes(MIGRATION_SUFFIX) }

    private companion object {
        private const val MIGRATION_SUFFIX = ".pgsql"
    }
}
