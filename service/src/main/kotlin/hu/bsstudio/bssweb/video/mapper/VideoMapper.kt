package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper

class VideoMapper(private val videoCrewMapper: VideoCrewMapper) {

    fun entityToModel(entity: VideoEntity): Video {
        return Video(
            id = entity.id,
            title = entity.title,
            uploadedAt = entity.uploadedAt,
            visible = entity.visible,
            archived = entity.archived,
        )
    }

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
            crew = entity.videoCrew.map(videoCrewMapper::entityToModel),
        )
    }

    fun modelToEntity(model: CreateVideo): VideoEntity {
        return VideoEntity(id = model.id, title = model.title)
    }
}
