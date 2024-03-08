package hu.bsstudio.bssweb.video.config

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import hu.bsstudio.bssweb.video.service.DefaultVideoService
import hu.bsstudio.bssweb.video.service.FileUpdatingVideoService
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class VideoServiceConfig(
    private val videoRepository: SimpleVideoRepository,
    private val detailedRepository: DetailedVideoRepository,
    private val fileClient: FileApiClient,
    private val videoCrewMapper: VideoCrewMapper,
) {
    @Bean
    @Primary
    fun videoService(defaultVideoService: VideoService): VideoService {
        return FileUpdatingVideoService(defaultVideoService, fileClient)
    }

    @Bean
    fun defaultVideoService(
        videoMapper: VideoMapper,
        labelRepository: LabelRepository,
    ): VideoService {
        return DefaultVideoService(videoRepository, detailedRepository, videoMapper, labelRepository)
    }

    @Bean
    fun videoMapper() = VideoMapper(videoCrewMapper)
}
