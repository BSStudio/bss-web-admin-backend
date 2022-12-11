package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import java.util.*

class DefaultVideoCrewService(
    private val repository: VideoCrewRepository,
    private val videoService: VideoService,
    private val mapper: VideoCrewMapper,
) : VideoCrewService {

    override fun getPositions() = repository.getPositions().sorted()

    override fun addPosition(videoCrew: VideoCrew): Optional<DetailedVideo> {
        return videoCrew
            .let(mapper::modelToEntity)
            .let(repository::save)
            .run { videoService.findVideoById(videoCrew.videoId) }
    }

    override fun removePosition(videoCrew: VideoCrew): Optional<DetailedVideo> {
        return videoCrew
            .let(mapper::modelToId)
            .let(repository::deleteById)
            .run { videoService.findVideoById(videoCrew.videoId) }
    }
}
