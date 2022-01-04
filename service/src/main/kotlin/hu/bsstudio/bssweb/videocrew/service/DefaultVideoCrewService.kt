package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository

class DefaultVideoCrewService(val repository: VideoCrewRepository) : VideoCrewService {
    override fun addPosition(videoCrew: VideoCrew): List<SimpleCrew> {
        videoCrew
            .let(this::mapModelToEntity)
            .let(repository::save)
        return repository.findAllByVideoId(videoCrew.videoId)
            .map { SimpleCrew(it.position, it.memberId) }
    }

    override fun removePosition(videoCrew: VideoCrew): List<SimpleCrew> {
        videoCrew
            .let(this::mapModelToEntity)
            .let(repository::deleteById)
        return repository.findAllByVideoId(videoCrew.videoId)
            .map { SimpleCrew(it.position, it.memberId) }
    }

    private fun mapModelToEntity(model: VideoCrew): VideoCrewEntity {
        return VideoCrewEntity(videoId = model.videoId, position = model.position, model.memberId)
    }
}
