package hu.bsstudio.bssweb.video.api

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/api/v1/video")
interface VideoApi {

    @GetMapping("/all")
    fun getAllVideos(): ResponseEntity<List<Video>>

    @GetMapping
    fun getAllVideos(pageable: Pageable): ResponseEntity<Page<Video>>

    @PostMapping
    fun createVideo(@RequestBody createVideo: CreateVideo): ResponseEntity<Video>

    @PutMapping("/visible")
    fun changeVideoVisibility(
        @RequestParam videoIds: List<UUID>,
        @RequestParam visible: Boolean
    ): ResponseEntity<List<UUID>>

    @GetMapping("/{videoId}")
    fun getVideo(@PathVariable videoId: UUID): ResponseEntity<DetailedVideo>

    @PutMapping("/{videoId}")
    fun updateVideo(
        @PathVariable videoId: UUID,
        @RequestBody updateVideo: UpdateVideo
    ): ResponseEntity<DetailedVideo>

    @DeleteMapping("/{videoId}")
    fun deleteVideo(@PathVariable videoId: UUID): ResponseEntity<Void>
}
