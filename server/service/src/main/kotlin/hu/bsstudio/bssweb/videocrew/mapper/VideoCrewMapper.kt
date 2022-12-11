package hu.bsstudio.bssweb.videocrew.mapper

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew

class VideoCrewMapper {

    fun modelToId(model: VideoCrew): VideoCrewEntityId {
        return VideoCrewEntityId(
                videoId = model.videoId,
                position = model.position,
                memberId = model.memberId,
        )
    }

    fun modelToEntity(model: VideoCrew): VideoCrewEntity {
        return VideoCrewEntity(id = this.modelToId(model))
    }

    fun entityToModel(entity: VideoCrewEntity): SimpleCrew {
        return SimpleCrew(
            position = entity.id.position,
            memberId = entity.id.memberId,
        )
    }
}
