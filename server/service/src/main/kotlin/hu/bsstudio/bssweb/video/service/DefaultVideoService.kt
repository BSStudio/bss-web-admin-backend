package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Transactional
open class DefaultVideoService(
    private val repository: SimpleVideoRepository,
    private val detailedRepository: DetailedVideoRepository,
    private val mapper: VideoMapper,
    private val labelRepository: LabelRepository,
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

    override fun changeVideoVisibility(
        videoIds: List<UUID>,
        visible: Boolean,
    ): List<UUID> {
        return repository.findAllById(videoIds)
            .map {
                it.visible = visible
                it
            }
            .map { repository.save(it) }
            .map { it.id }
    }

    override fun findVideoById(videoId: UUID): Optional<DetailedVideo> {
        return detailedRepository.findById(videoId)
            .map(mapper::entityToModel)
    }

    override fun updateVideo(
        videoId: UUID,
        updateVideo: UpdateVideo,
    ): Optional<DetailedVideo> {
        val labels = labelRepository.findAllByNameIn(updateVideo.labels)
        return detailedRepository.findById(videoId)
            .map { mapper.updateToEntity(it, updateVideo, labels) }
            .map(detailedRepository::save)
            .map(mapper::entityToModel)
    }

    override fun deleteVideoById(videoId: UUID) = repository.deleteById(videoId)
}
