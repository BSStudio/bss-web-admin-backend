package hu.bsstudio.bssweb.videocrew.config

import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import hu.bsstudio.bssweb.videocrew.service.DefaultVideoCrewService
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VideoCrewServiceConfig(val repository: VideoCrewRepository) {

    @Bean
    fun defaultVideoCrewService():VideoCrewService = DefaultVideoCrewService(repository)
}
