package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository

class DefaultVideoCrewService(
    private val repository: VideoCrewRepository,
    private val mapper: VideoCrewMapper
) : VideoCrewService {

    override fun addPosition(videoCrew: VideoCrew): List<SimpleCrew> {
        videoCrew
            .let(mapper::modelToEntity)
            .let(repository::save)
        return repository.findAllByVideoId(videoCrew.videoId)
            .map(mapper::entityToModel)
    }

    override fun removePosition(videoCrew: VideoCrew): List<SimpleCrew> {
        videoCrew
            .let(mapper::modelToEntity)
            .let(repository::deleteById)
        return repository.findAllByVideoId(videoCrew.videoId)
            .map(mapper::entityToModel)
    }
}
