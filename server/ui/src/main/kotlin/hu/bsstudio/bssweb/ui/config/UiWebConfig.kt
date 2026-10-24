package hu.bsstudio.bssweb.ui.config

import jakarta.servlet.Filter
import jakarta.servlet.http.HttpServletRequest
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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

/**
 * English by default; `?lang=hu` switches to Hungarian and remembers it in a cookie.
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
     * An admin address with a trailing slash, such as `/videos/`, redirects to the one without it. Spring no longer
     * treats them as the same page, and this keeps one address per page. It runs before the login, so the address
     * saved for after the login is already the right one. The API's addresses are left as they are.
     */
    @Bean
    fun trailingSlashRedirect(): FilterRegistrationBean<Filter> {
        val redirect = UrlHandlerFilter.trailingSlashHandler("/**").redirect(HttpStatus.PERMANENT_REDIRECT).build()
        val filter =
            Filter { request, response, chain ->
                if (UiPaths.isUi(request as HttpServletRequest)) {
                    redirect.doFilter(request, response, chain)
                } else {
                    chain.doFilter(request, response)
                }
            }
        return FilterRegistrationBean(filter).apply { order = Ordered.HIGHEST_PRECEDENCE }
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(LocaleChangeInterceptor().apply { paramName = LOCALE_PARAM })
            .addPathPatterns("/**")
            .excludePathPatterns(UiPaths.EXCLUDED)
    }

    companion object {
        private const val LOCALE_COOKIE = "bss-admin-lang"

        /** The query parameter that switches the language. */
        const val LOCALE_PARAM = "lang"
    }
}
