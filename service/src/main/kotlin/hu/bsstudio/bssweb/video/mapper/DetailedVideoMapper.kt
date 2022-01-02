package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.DetailedVideo

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
        )
    }

    fun modelToEntity(model: DetailedVideo): DetailedVideoEntity {
        return DetailedVideoEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            videoUrl = model.videoUrl,
            thumbnailUrl = model.thumbnailUrl,
            uploadedAt = model.uploadedAt,
            visible = model.visible,
            archived = model.archived,
        )
    }
}
