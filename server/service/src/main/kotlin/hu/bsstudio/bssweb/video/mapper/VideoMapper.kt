package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import java.util.UUID

class VideoMapper(private val videoCrewMapper: VideoCrewMapper, private val idMapper: () -> UUID = UUID::randomUUID) {

    fun entityToModel(entity: VideoEntity): Video {
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
            videoUrl = entity.videoUrl,
            thumbnailUrl = entity.thumbnailUrl,
            uploadedAt = entity.uploadedAt,
            visible = entity.visible,
            crew = entity.videoCrew.map(videoCrewMapper::entityToModel),
        )
    }

    fun modelToEntity(model: CreateVideo): VideoEntity {
        return VideoEntity(id = idMapper.invoke(), url = model.url, title = model.title)
    }
}
