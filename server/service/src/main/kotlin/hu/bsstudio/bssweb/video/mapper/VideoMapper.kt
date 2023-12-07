package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper

class VideoMapper(private val videoCrewMapper: VideoCrewMapper) {
    fun entityToModel(entity: SimpleVideoEntity): Video {
        return Video(
            id = entity.id,
            url = entity.url,
            title = entity.title,
            uploadedAt = entity.uploadedAt,
            visible = entity.visible,
        )
    }

    fun entityToModel(entity: DetailedVideoEntity): DetailedVideo {
        return DetailedVideo(
            id = entity.id,
            url = entity.url,
            title = entity.title,
            description = entity.description,
            uploadedAt = entity.uploadedAt,
            visible = entity.visible,
            crew = entity.videoCrew.map(videoCrewMapper::entityToModel),
        )
    }

    fun modelToEntity(model: CreateVideo): SimpleVideoEntity {
        return SimpleVideoEntity(url = model.url, title = model.title)
    }

    fun updateToEntity(
        videoEntity: DetailedVideoEntity,
        updateVideo: UpdateVideo,
    ): DetailedVideoEntity {
        videoEntity.url = updateVideo.url
        videoEntity.title = updateVideo.title
        videoEntity.description = updateVideo.description
        videoEntity.uploadedAt = updateVideo.uploadedAt
        videoEntity.visible = updateVideo.visible
        return videoEntity
    }
}
