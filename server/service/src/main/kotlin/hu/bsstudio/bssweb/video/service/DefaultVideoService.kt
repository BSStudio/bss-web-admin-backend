package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.VideoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

class DefaultVideoService(
    private val repository: VideoRepository,
    private val detailedRepository: DetailedVideoRepository,
    private val mapper: VideoMapper,
) : VideoService {

    override fun findAllVideos(): List<Video> {
        return repository.findAll()
            .map(mapper::entityToModel)
    }

    override fun findAllVideos(pageable: Pageable): Page<Video> {
        return repository.findAll(pageable)
            .map(mapper::entityToModel)
    }

    override fun insertVideo(createVideo: CreateVideo): Video {
        return createVideo
            .let(mapper::modelToEntity)
            .let(repository::save)
            .let(mapper::entityToModel)
    }

    override fun changeVideoVisibility(videoIds: List<UUID>, visible: Boolean): List<UUID> {
        return repository.findAllById(videoIds)
            .map { it.copy(visible = visible) }
            .map(repository::save)
            .map(VideoEntity::id)
    }

    override fun findVideoById(videoId: UUID): Optional<DetailedVideo> {
        return detailedRepository.findById(videoId)
            .map(mapper::entityToModel)
    }

    override fun updateVideo(videoId: UUID, updateVideo: UpdateVideo): Optional<DetailedVideo> {
        return detailedRepository.findById(videoId)
            .map { updateVideo(it, updateVideo) }
            .map(detailedRepository::save)
            .map(mapper::entityToModel)
    }

    override fun deleteVideoById(videoId: UUID) = repository.deleteById(videoId)

    private fun updateVideo(videoEntity: DetailedVideoEntity, updateVideo: UpdateVideo): DetailedVideoEntity {
        return videoEntity.copy(
            url = updateVideo.url,
            title = updateVideo.title,
            description = updateVideo.description,
            videoUrl = updateVideo.videoUrl,
            thumbnailUrl = updateVideo.thumbnailUrl,
            uploadedAt = updateVideo.uploadedAt,
            visible = updateVideo.visible,
        )
    }
}
