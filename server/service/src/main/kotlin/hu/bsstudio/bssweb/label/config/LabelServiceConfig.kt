package hu.bsstudio.bssweb.label.config

import hu.bsstudio.bssweb.label.mapper.LabelMapper
import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.label.service.DefaultLabelService
import hu.bsstudio.bssweb.label.service.LabelService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LabelServiceConfig(
    private val labelRepository: LabelRepository,
) {
    @Bean
    fun labelService(labelMapper: LabelMapper): LabelService {
        return DefaultLabelService(labelRepository, labelMapper)
    }

    @Bean
    fun labelMapper() = LabelMapper()
}
