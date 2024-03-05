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
            urls = entity.urls,
            title = entity.title,
            shootingDateStart = entity.shootingDateStart,
            shootingDateEnd = entity.shootingDateEnd,
            visible = entity.visible,
        )
    }

    fun entityToModel(entity: DetailedVideoEntity): DetailedVideo {
        return DetailedVideo(
            id = entity.id,
            urls = entity.urls,
            title = entity.title,
            description = entity.description,
            shootingDateStart = entity.shootingDateStart,
            shootingDateEnd = entity.shootingDateEnd,
            visible = entity.visible,
            crew = entity.videoCrew.map(videoCrewMapper::entityToModel),
            categories = entity.categories
        )
    }

    fun modelToEntity(model: CreateVideo): SimpleVideoEntity {
        return SimpleVideoEntity(title = model.title)
    }

    fun updateToEntity(
        videoEntity: DetailedVideoEntity,
        updateVideo: UpdateVideo,
    ): DetailedVideoEntity {
        videoEntity.urls = updateVideo.urls
        videoEntity.title = updateVideo.title
        videoEntity.description = updateVideo.description
        videoEntity.shootingDateStart = updateVideo.shootingDateStart
        videoEntity.shootingDateEnd = updateVideo.shootingDateEnd
        videoEntity.visible = updateVideo.visible
        videoEntity.categories = updateVideo.categories
        return videoEntity
    }
}
