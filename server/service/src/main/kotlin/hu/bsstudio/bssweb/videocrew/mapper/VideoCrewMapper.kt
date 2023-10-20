package hu.bsstudio.bssweb.videocrew.mapper

import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest

class VideoCrewMapper(private val memberMapper: MemberMapper) {

    fun modelToId(model: VideoCrewRequest): VideoCrewEntityId {
        return VideoCrewEntityId(
            videoId = model.videoId,
            position = model.position,
            memberId = model.memberId
        )
    }

    fun modelToEntity(model: VideoCrewRequest): VideoCrewEntity {
        return VideoCrewEntity(this.modelToId(model))
    }

    fun entityToModel(entity: DetailedVideoCrewEntity): VideoCrew {
        return VideoCrew(
            videoId = entity.id.videoId,
            position = entity.id.position,
            member = memberMapper.entityToModel(entity.member)
        )
    }
}
