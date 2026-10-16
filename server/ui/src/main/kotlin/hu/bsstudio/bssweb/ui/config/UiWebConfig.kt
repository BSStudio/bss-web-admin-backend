package hu.bsstudio.bssweb.ui.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.resource.LiteWebJarsResourceResolver
import java.util.Locale

/**
 * English by default; `?lang=hu` switches to Hungarian and remembers it in a cookie.
 * WebJars are served under `/admin/webjars` without version numbers in the path.
 */
@Configuration
class UiWebConfig : WebMvcConfigurer {
    @Bean
    fun localeResolver(): LocaleResolver = CookieLocaleResolver(LOCALE_COOKIE).apply { setDefaultLocale(Locale.ENGLISH) }

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

    private companion object {
        private const val LOCALE_COOKIE = "bss-admin-lang"
        private const val LOCALE_PARAM = "lang"
    }
}
