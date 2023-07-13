package hu.bsstudio.bssweb.video.controller

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.operation.VideoOperation
import hu.bsstudio.bssweb.video.service.VideoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.UUID

@RestController
class VideoController(private val service: VideoService) : VideoOperation {

    override fun getAllVideos(): ResponseEntity<List<Video>> {
        return service.findAllVideos()
            .let { ResponseEntity.ok(it) }
    }

    override fun getAllVideos(pageable: Pageable): ResponseEntity<Page<Video>> {
        return service.findAllVideos(pageable)
            .let { ResponseEntity.ok(it) }
    }

    @PreAuthorize("hasAnyRole('asd', 'asd')")
    override fun createVideo(createVideo: CreateVideo): ResponseEntity<Video> {
        return service.insertVideo(createVideo)
            .let { ResponseEntity.created(locationUri(it.id)).body(it) }
    }

    override fun changeVideoVisibility(videoIds: List<UUID>, visible: Boolean): ResponseEntity<List<UUID>> {
        return service.changeVideoVisibility(videoIds, visible)
            .let { ResponseEntity.ok(it) }
    }

    override fun getVideo(videoId: UUID): ResponseEntity<DetailedVideo> {
        return service.findVideoById(videoId)
            .let { ResponseEntity.of(it) }
    }

    override fun updateVideo(videoId: UUID, updateVideo: UpdateVideo): ResponseEntity<DetailedVideo> {
        return service.updateVideo(videoId, updateVideo)
            .let { ResponseEntity.of(it) }
    }

    override fun deleteVideo(videoId: UUID): ResponseEntity<Void> {
        service.deleteVideoById(videoId)
        return ResponseEntity.noContent().build()
    }

    private fun locationUri(id: UUID) = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(id)
        .toUri()
}
