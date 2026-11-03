package hu.bsstudio.bssweb.video.controller

import hu.bsstudio.bssweb.revision.controller.restoring
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.operation.VideoOperation
import hu.bsstudio.bssweb.video.service.VideoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.UUID

@RestController
class VideoController(
    private val service: VideoService,
) : VideoOperation {
    override fun getAllVideos(): ResponseEntity<List<Video>> =
        service
            .findAllVideos()
            .let { ResponseEntity.ok(it) }

    override fun getAllVideos(pageable: Pageable): ResponseEntity<Page<Video>> =
        service
            .findAllVideos(pageable)
            .let { ResponseEntity.ok(it) }

    override fun createVideo(createVideo: CreateVideo): ResponseEntity<Video> =
        service
            .insertVideo(createVideo)
            .let { ResponseEntity.created(locationUri(it.id)).body(it) }

    override fun changeVideoVisibility(
        videoIds: List<UUID>,
        visible: Boolean,
    ): ResponseEntity<List<UUID>> =
        service
            .changeVideoVisibility(videoIds, visible)
            .let { ResponseEntity.ok(it) }

    override fun getVideo(videoId: UUID): ResponseEntity<DetailedVideo> =
        service
            .findVideoById(videoId)
            .let { ResponseEntity.of(it) }

    override fun updateVideo(
        videoId: UUID,
        updateVideo: UpdateVideo,
    ): ResponseEntity<DetailedVideo> =
        service
            .updateVideo(videoId, updateVideo)
            .let { ResponseEntity.of(it) }

    override fun deleteVideo(videoId: UUID): ResponseEntity<Unit> {
        service.deleteVideoById(videoId)
        return ResponseEntity.noContent().build()
    }

    override fun getDeletedVideos(pageable: Pageable): ResponseEntity<Page<Video>> =
        service
            .findDeletedVideos(pageable)
            .let { ResponseEntity.ok(it) }

    override fun restoreVideo(videoId: UUID): ResponseEntity<DetailedVideo> = restoring { service.restoreVideo(videoId) }

    override fun getVideoRevisions(videoId: UUID): ResponseEntity<List<RevisionInfo>> =
        service
            .findVideoRevisions(videoId)
            .let { ResponseEntity.ok(it) }

    override fun getVideoRevision(
        videoId: UUID,
        revision: Long,
    ): ResponseEntity<DetailedVideo> =
        service
            .findVideoRevision(videoId, revision)
            .let { ResponseEntity.of(it) }

    override fun restoreVideoRevision(
        videoId: UUID,
        revision: Long,
    ): ResponseEntity<DetailedVideo> = restoring { service.restoreVideoRevision(videoId, revision) }

    private fun locationUri(id: UUID) =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
}
