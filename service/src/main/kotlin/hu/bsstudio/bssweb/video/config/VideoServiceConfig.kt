package hu.bsstudio.bssweb.video.config

import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.VideoRepository
import hu.bsstudio.bssweb.video.service.DefaultVideoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VideoServiceConfig(
    private val videoRepository: VideoRepository,
    private val detailedRepository: DetailedVideoRepository
) {

    @Bean
    fun defaultVideoService(): DefaultVideoService {
        return DefaultVideoService(videoRepository, detailedRepository)
    }
}
