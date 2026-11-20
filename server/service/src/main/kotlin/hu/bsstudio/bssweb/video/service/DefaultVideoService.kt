package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.revision.mapper.RevisionMapper
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.repository.VideoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.Optional
import java.util.UUID

@Transactional
class DefaultVideoService(
    private val repository: VideoRepository,
    private val mapper: VideoMapper,
    private val labelRepository: LabelRepository,
    private val revisionMapper: RevisionMapper,
) : VideoService {
    override fun findAllVideos(): List<Video> =
        repository
            .findAll()
            .map(mapper::entityToModel)

    override fun findAllVideos(pageable: Pageable): Page<Video> =
        repository
            .findAll(pageable)
            .map(mapper::entityToModel)

    override fun insertVideo(createVideo: CreateVideo): Video =
        createVideo
            .let(mapper::modelToEntity)
            .let(repository::save)
            .let(mapper::entityToModel)

    override fun changeVideoVisibility(
        videoIds: List<UUID>,
        visible: Boolean,
    ): List<UUID> =
        repository
            .findAllById(videoIds)
            .map {
                it.visible = visible
                it
            }.map(repository::save)
            .map { it.id }

    override fun findVideoById(videoId: UUID): Optional<DetailedVideo> =
        repository
            .findById(videoId)
            .map(mapper::entityToDetailedModel)

    override fun updateVideo(
        videoId: UUID,
        updateVideo: UpdateVideo,
    ): Optional<DetailedVideo> {
        val labels = labelRepository.findAllByNameIn(updateVideo.labels)
        return repository
            .findById(videoId)
            .map { mapper.updateToEntity(it, updateVideo, labels) }
            .map(repository::save)
            .map(mapper::entityToDetailedModel)
    }

    override fun deleteVideoById(videoId: UUID) =
        repository.findById(videoId).ifPresent {
            it.deletedAt = Instant.now()
            repository.save(it)
        }

    override fun findDeletedVideos(pageable: Pageable): Page<Video> =
        repository
            .findAllDeleted(pageable)
            .map(mapper::entityToModel)

    override fun restoreVideo(videoId: UUID): Optional<DetailedVideo> =
        repository
            .findDeletedById(videoId)
            .map {
                it.deletedAt = null
                it
            }.map(repository::save)
            .map(mapper::entityToDetailedModel)

    override fun findVideoRevisions(videoId: UUID): List<RevisionInfo> =
        revisionMapper.revisionsToModel(repository.findRevisions(videoId), VideoEntity::deletedAt)

    override fun findVideoRevision(
        videoId: UUID,
        revision: Long,
    ): Optional<DetailedVideo> =
        repository
            .findRevision(videoId, revision)
            .map { mapper.revisionToModel(it.entity) }

    override fun restoreVideoRevision(
        videoId: UUID,
        revision: Long,
    ): Optional<DetailedVideo> =
        repository
            .findRevision(videoId, revision)
            .flatMap { updateVideo(videoId, mapper.entityToUpdate(it.entity)) }
}
