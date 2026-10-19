package hu.bsstudio.bssweb.ui.config

import org.springframework.boot.security.oauth2.client.autoconfigure.ConditionalOnOAuth2ClientRegistrationProperties

/**
 * Turns a bean of the admin UI on only when its login is configured, that is, when a client registration is
 * configured, whatever its id (`authentik` in production). Every bean of this module carries it, so without that
 * setting nothing of the UI is mapped or registered. The error page template (`templates/error.html`) is still on the
 * classpath; Spring Boot then renders it without the admin details.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ConditionalOnOAuth2ClientRegistrationProperties
annotation class ConditionalOnAdminUi
