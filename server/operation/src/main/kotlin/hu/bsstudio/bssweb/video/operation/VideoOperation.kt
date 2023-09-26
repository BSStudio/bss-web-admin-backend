package hu.bsstudio.bssweb.video.operation

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

interface VideoOperation {

    @GetMapping("/api/v1/video/all")
    fun getAllVideos(): ResponseEntity<List<Video>>

    @GetMapping("/api/v1/video")
    fun getAllVideos(pageable: Pageable): ResponseEntity<Page<Video>>

    @PostMapping("/api/v1/video")
    fun createVideo(@RequestBody createVideo: CreateVideo): ResponseEntity<Video>

    @PutMapping("/api/v1/video/visible")
    fun changeVideoVisibility(
        @RequestParam videoIds: List<UUID>,
        @RequestParam visible: Boolean
    ): ResponseEntity<List<UUID>>

    @GetMapping("/api/v1/video/{videoId}")
    fun getVideo(@PathVariable videoId: UUID): ResponseEntity<DetailedVideo>

    @PutMapping("/api/v1/video/{videoId}")
    fun updateVideo(
        @PathVariable videoId: UUID,
        @RequestBody updateVideo: UpdateVideo
    ): ResponseEntity<DetailedVideo>

    @DeleteMapping("/api/v1/video/{videoId}")
    fun deleteVideo(@PathVariable videoId: UUID): ResponseEntity<Void>
}
