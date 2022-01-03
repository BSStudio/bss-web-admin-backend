package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew

class DetailedVideoMapper {
    fun entityToModel(entity: DetailedVideoEntity): DetailedVideo {
        return DetailedVideo(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            videoUrl = entity.videoUrl,
            thumbnailUrl = entity.thumbnailUrl,
            uploadedAt = entity.uploadedAt,
            visible = entity.visible,
            archived = entity.archived,
            crew = entity.videoCrew.map { SimpleCrew(it.position, it.memberId) },
        )
    }
}
