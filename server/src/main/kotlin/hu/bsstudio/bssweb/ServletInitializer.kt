package hu.bsstudio.bssweb

import jakarta.servlet.Filter
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

class ServletInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getServletMappings(): Array<String> {
        return arrayOf("/")
    }

    override fun getRootConfigClasses(): Array<Class<*>>? {
        return null
    }

    override fun getServletConfigClasses(): Array<Class<*>>? {
        return arrayOf(BssWebApplication::class.java)
    }
}
