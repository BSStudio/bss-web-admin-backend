package hu.bsstudio.bssweb.config

import org.hibernate.envers.configuration.EnversSettings
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configures Hibernate Envers, which records the history of the `@Audited` entities.
 *
 * Spring Boot makes the repositories `RevisionRepository`s once Spring Data Envers is on the classpath. Flyway
 * creates the audit tables, `<table>_aud`. A delete keeps the entity's last state in its revision, so the history
 * of a deleted item ends with what it was.
 */
@Configuration
class EnversConfig {
    @Bean
    fun enversSettings() =
        HibernatePropertiesCustomizer {
            it[EnversSettings.AUDIT_TABLE_SUFFIX] = "_aud"
            it[EnversSettings.STORE_DATA_AT_DELETE] = true
        }
}
