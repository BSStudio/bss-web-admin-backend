package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper

class VideoMapper(
    private val videoCrewMapper: VideoCrewMapper,
) {
    fun entityToModel(entity: SimpleVideoEntity): Video =
        Video(
            id = entity.id,
            title = entity.title,
            urls = entity.urls,
            description = entity.description,
            shootingDateStart = entity.shootingDateStart,
            shootingDateEnd = entity.shootingDateEnd,
            visible = entity.visible,
        )

    fun entityToModel(entity: DetailedVideoEntity): DetailedVideo =
        DetailedVideo(
            id = entity.id,
            title = entity.title,
            urls = entity.urls,
            description = entity.description,
            shootingDateStart = entity.shootingDateStart,
            shootingDateEnd = entity.shootingDateEnd,
            visible = entity.visible,
            labels = entity.labels.map { it.name },
            crew = entity.videoCrew.map(videoCrewMapper::entityToModel),
        )

    fun modelToEntity(model: CreateVideo): SimpleVideoEntity = SimpleVideoEntity(title = model.title)

    fun updateToEntity(
        videoEntity: DetailedVideoEntity,
        updateVideo: UpdateVideo,
        labels: List<LabelEntity>,
    ): DetailedVideoEntity {
        videoEntity.title = updateVideo.title
        videoEntity.urls = updateVideo.urls
        videoEntity.description = updateVideo.description
        videoEntity.shootingDateStart = updateVideo.shootingDateStart
        videoEntity.shootingDateEnd = updateVideo.shootingDateEnd
        videoEntity.visible = updateVideo.visible
        videoEntity.labels = labels
        return videoEntity
    }
}
