package hu.bsstudio.bssweb.ui.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

/**
 * Turns a bean of the admin UI on only when its login is configured, that is, when the `authentik` client
 * registration has a client id. Every bean of this module carries it, so without that setting nothing of the UI
 * is mapped or registered. The error page template (`templates/error.html`) is still on the classpath; Spring Boot
 * then renders it without the admin details.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ConditionalOnProperty(prefix = "spring.security.oauth2.client.registration.authentik", name = ["client-id"])
annotation class ConditionalOnAdminUi
