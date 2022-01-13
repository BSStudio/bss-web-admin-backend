package hu.bsstudio.bssweb.video.config

import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.VideoRepository
import hu.bsstudio.bssweb.video.service.DefaultVideoService
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VideoServiceConfig(
    private val videoRepository: VideoRepository,
    private val detailedRepository: DetailedVideoRepository
) {

    @Bean
    fun defaultVideoService(videoMapper: VideoMapper): VideoService {
        return DefaultVideoService(videoRepository, detailedRepository, videoMapper)
    }

    @Bean
    fun videoMapper(videoCrewMapper: VideoCrewMapper) = VideoMapper(videoCrewMapper)
}
