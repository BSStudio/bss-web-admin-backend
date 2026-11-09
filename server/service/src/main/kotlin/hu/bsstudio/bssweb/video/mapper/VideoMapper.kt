package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper

class VideoMapper(
    private val videoCrewMapper: VideoCrewMapper,
) {
    fun entityToModel(entity: VideoEntity): Video =
        Video(
            id = entity.id,
            title = entity.title,
            urls = entity.urls,
            description = entity.description,
            shootingDateStart = entity.shootingDateStart,
            shootingDateEnd = entity.shootingDateEnd,
            visible = entity.visible,
        )

    fun entityToDetailedModel(entity: VideoEntity): DetailedVideo =
        revisionToModel(entity).copy(crew = entity.videoCrew.map(videoCrewMapper::entityToModel))

    /**
     * A video as it was at a revision. Its crew has no history, so it is left empty. A revision's URLs are a lazy list
     * that can only be read while its session is open, so they are copied here and in [entityToUpdate].
     */
    fun revisionToModel(entity: VideoEntity): DetailedVideo =
        DetailedVideo(
            id = entity.id,
            title = entity.title,
            urls = entity.urls.toList(),
            description = entity.description,
            shootingDateStart = entity.shootingDateStart,
            shootingDateEnd = entity.shootingDateEnd,
            visible = entity.visible,
            labels = entity.labels.map { it.name },
            crew = emptyList(),
        )

    fun entityToUpdate(entity: VideoEntity): UpdateVideo =
        UpdateVideo(
            title = entity.title,
            urls = entity.urls.toList(),
            description = entity.description,
            shootingDateStart = entity.shootingDateStart,
            shootingDateEnd = entity.shootingDateEnd,
            visible = entity.visible,
            labels = entity.labels.map { it.name },
        )

    fun modelToEntity(model: CreateVideo): VideoEntity = VideoEntity(title = model.title)

    fun updateToEntity(
        videoEntity: VideoEntity,
        updateVideo: UpdateVideo,
        labels: List<LabelEntity>,
    ): VideoEntity {
        videoEntity.title = updateVideo.title
        // a merge refills the entity's collection in place, so it can't be a read-only list such as emptyList()
        videoEntity.urls = updateVideo.urls.toMutableList()
        videoEntity.description = updateVideo.description
        videoEntity.shootingDateStart = updateVideo.shootingDateStart
        videoEntity.shootingDateEnd = updateVideo.shootingDateEnd
        videoEntity.visible = updateVideo.visible
        videoEntity.labels = labels
        return videoEntity
    }
}
