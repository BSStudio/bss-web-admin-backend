package hu.bsstudio.bssweb.video.operation

import hu.bsstudio.bssweb.revision.model.RevisionInfo
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.annotation.PutExchange
import java.util.UUID

@HttpExchange("/api/v1/video")
interface VideoOperation {
    @GetExchange("/all")
    fun getAllVideos(): ResponseEntity<List<Video>>

    @GetExchange
    fun getAllVideos(pageable: Pageable): ResponseEntity<Page<Video>>

    @PostExchange
    fun createVideo(
        @RequestBody createVideo: CreateVideo,
    ): ResponseEntity<Video>

    @PutExchange("/visible")
    fun changeVideoVisibility(
        @RequestParam videoIds: List<UUID>,
        @RequestParam visible: Boolean,
    ): ResponseEntity<List<UUID>>

    @GetExchange("/{videoId}")
    fun getVideo(
        @PathVariable videoId: UUID,
    ): ResponseEntity<DetailedVideo>

    @PutExchange("/{videoId}")
    fun updateVideo(
        @PathVariable videoId: UUID,
        @RequestBody updateVideo: UpdateVideo,
    ): ResponseEntity<DetailedVideo>

    @DeleteExchange("/{videoId}")
    fun deleteVideo(
        @PathVariable videoId: UUID,
    ): ResponseEntity<Unit>

    @GetExchange("/deleted")
    fun getDeletedVideos(pageable: Pageable): ResponseEntity<Page<Video>>

    @PostExchange("/{videoId}/restore")
    fun restoreVideo(
        @PathVariable videoId: UUID,
    ): ResponseEntity<DetailedVideo>

    @GetExchange("/{videoId}/revisions")
    fun getVideoRevisions(
        @PathVariable videoId: UUID,
    ): ResponseEntity<List<RevisionInfo>>

    @GetExchange("/{videoId}/revisions/{revision}")
    fun getVideoRevision(
        @PathVariable videoId: UUID,
        @PathVariable revision: Long,
    ): ResponseEntity<DetailedVideo>

    @PostExchange("/{videoId}/revisions/{revision}/restore")
    fun restoreVideoRevision(
        @PathVariable videoId: UUID,
        @PathVariable revision: Long,
    ): ResponseEntity<DetailedVideo>
}
