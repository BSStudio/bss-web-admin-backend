package hu.bsstudio.bssweb.videocrew.config

import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import hu.bsstudio.bssweb.videocrew.service.DefaultVideoCrewService
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VideoCrewServiceConfig(
    private val repository: VideoCrewRepository,
    private val videoService: VideoService
) {

    @Bean
    fun defaultVideoCrewService(videoCrewMapper: VideoCrewMapper): VideoCrewService {
        return DefaultVideoCrewService(repository, videoService, videoCrewMapper)
    }

    @Bean
    fun videoCrewMapper() = VideoCrewMapper()
}
