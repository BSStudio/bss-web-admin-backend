package hu.bsstudio.bssweb.revision.config

import hu.bsstudio.bssweb.revision.mapper.RevisionMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RevisionMapperConfig {
    @Bean
    fun revisionMapper() = RevisionMapper()
}
