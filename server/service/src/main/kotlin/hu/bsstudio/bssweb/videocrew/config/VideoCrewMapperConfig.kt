package hu.bsstudio.bssweb.videocrew.config

import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VideoCrewMapperConfig {
    @Bean
    fun videoCrewMapper() = VideoCrewMapper()
}
