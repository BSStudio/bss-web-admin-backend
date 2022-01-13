package hu.bsstudio.bssweb.videocrew.mapper

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew

class VideoCrewMapper {

    fun modelToEntity(model: VideoCrew): VideoCrewEntity {
        return VideoCrewEntity(
            videoId = model.videoId,
            position = model.position,
            memberId = model.memberId,
        )
    }

    fun entityToModel(entity: VideoCrewEntity): SimpleCrew {
        return SimpleCrew(
            position = entity.position,
            memberId = entity.memberId,
        )
    }
}
