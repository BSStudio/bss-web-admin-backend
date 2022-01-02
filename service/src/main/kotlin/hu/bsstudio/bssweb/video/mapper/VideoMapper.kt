package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.model.Video

class VideoMapper {

    fun entityToModel(entity: VideoEntity): Video {
        return Video(
            id = entity.id,
            title = entity.title,
            uploadedAt = entity.uploadedAt,
            visible = entity.visible,
            archived = entity.archived,
        )
    }

    fun modelToEntity(model: Video): VideoEntity {
        return VideoEntity(
            id = model.id,
            title = model.title,
            uploadedAt = model.uploadedAt,
            visible = model.visible,
            archived = model.archived,
        )
    }
}
