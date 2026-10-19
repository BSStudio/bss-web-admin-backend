package hu.bsstudio.bssweb.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/** The Swagger UI fetches this stable URL, so it must revalidate after each deployment. */
@Configuration
class OpenApiResourceConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/open-api.yaml")
            .addResourceLocations("classpath:/static/")
            .setCacheControl(CacheControl.noCache())
    }
}
