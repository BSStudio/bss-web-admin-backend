package hu.bsstudio.bssweb.ui.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.core.Ordered
import org.springframework.http.HttpStatus
import org.springframework.web.filter.UrlHandlerFilter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.resource.LiteWebJarsResourceResolver

/**
 * English by default; `?lang=hu` switches to Hungarian and remembers it in a cookie.
 * WebJars are served under `/admin/webjars` without version numbers in the path.
 */
@Configuration
@ConditionalOnAdminUi
class UiWebConfig : WebMvcConfigurer {
    @Bean
    fun localeResolver(): LocaleResolver = AdminLocaleResolver(LOCALE_COOKIE)

    /**
     * The UI's texts: English in `messages.properties`, Hungarian in `messages_hu.properties`. There is no
     * `messages_en`, so without turning off the fallback to the server's own locale, an English page on a server
     * running in Hungarian would get the Hungarian texts. It replaces Spring Boot's message source, which only
     * the UI and validation messages use.
     */
    @Bean
    fun messageSource(): MessageSource =
        ResourceBundleMessageSource().apply {
            setBasename("messages")
            setDefaultEncoding(Charsets.UTF_8.name())
            setFallbackToSystemLocale(false)
        }

    /**
     * An admin address with a trailing slash, such as `/admin/`, redirects to the one without it. Spring no longer
     * treats them as the same page, and this keeps one address per page. It runs before the login, so the address
     * saved for after the login is already the right one.
     */
    @Bean
    fun trailingSlashRedirect(): FilterRegistrationBean<UrlHandlerFilter> =
        FilterRegistrationBean(
            UrlHandlerFilter.trailingSlashHandler("/admin/**").redirect(HttpStatus.PERMANENT_REDIRECT).build(),
        ).apply { order = Ordered.HIGHEST_PRECEDENCE }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(LocaleChangeInterceptor().apply { paramName = LOCALE_PARAM })
            .addPathPatterns("/admin/**")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/admin/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
            .resourceChain(true)
            .addResolver(LiteWebJarsResourceResolver())
    }

    companion object {
        private const val LOCALE_COOKIE = "bss-admin-lang"

        /** The query parameter that switches the language. */
        const val LOCALE_PARAM = "lang"
    }
}
