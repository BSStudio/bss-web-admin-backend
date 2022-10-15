package hu.bsstudio.bssweb.video.controller

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/video")
class VideoController(private val service: VideoService) {

    @GetMapping
    fun getAllVideos(pageable: Pageable): ResponseEntity<Page<Video>> {
        return service.findAllVideos(pageable)
            .let { ResponseEntity.ok(it) }
    }

    @PostMapping
    fun createVideo(@RequestBody createVideo: CreateVideo): ResponseEntity<Video> {
        return service.insertVideo(createVideo)
            .let { ResponseEntity(it, HttpStatus.CREATED) }
    }

    @PutMapping("/visible")
    fun changeVideoVisibility(
        @RequestParam videoIds: List<UUID>,
        @RequestParam visible: Boolean
    ): ResponseEntity<List<UUID>> {
        return service.changeVideoVisibility(videoIds, visible)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{videoId}")
    fun getVideo(@PathVariable videoId: UUID): ResponseEntity<DetailedVideo> {
        return service.findVideoById(videoId)
            .let { ResponseEntity.of(it) }
    }

    @PutMapping("/{videoId}")
    fun updateVideo(
        @PathVariable videoId: UUID,
        @RequestBody updateVideo: UpdateVideo
    ): ResponseEntity<DetailedVideo> {
        return service.updateVideo(videoId, updateVideo)
            .let { ResponseEntity.of(it) }
    }

    @DeleteMapping("/{videoId}")
    fun deleteVideo(@PathVariable videoId: UUID) {
        service.deleteVideoById(videoId)
    }
}
