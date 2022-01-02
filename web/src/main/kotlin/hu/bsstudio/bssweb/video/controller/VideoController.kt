package hu.bsstudio.bssweb.video.controller

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
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

@RestController
@RequestMapping("/api/video")
class VideoController(val service: VideoService) {

    @GetMapping
    fun getAllVideos(@RequestParam page: Int, @RequestParam size: Int): ResponseEntity<List<Video>> {
        return service.findAllVideos(page, size)
            .let { ResponseEntity.ok(it) }
    }

    @PostMapping
    fun createVideo(@RequestBody createVideo: CreateVideo): ResponseEntity<Video> {
        return service.insertVideo(createVideo)
            .let { ResponseEntity.ok(it) }
    }

    @PutMapping("/archive")
    fun archiveVideos(
        @RequestParam videoIds: List<String>,
        @RequestParam unArchive: Boolean
    ): ResponseEntity<List<String>> {
        return service.archiveVideos(videoIds, unArchive)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{videoId}")
    fun getVideo(@PathVariable videoId: String): ResponseEntity<DetailedVideo> {
        return service.findVideoById(videoId)
            .let { ResponseEntity.of(it) }
    }

    @PutMapping
    fun updateVideo(@RequestBody updateVideo: UpdateVideo): ResponseEntity<DetailedVideo> {
        return service.updateVideo(updateVideo)
            .let { ResponseEntity.of(it) }
    }

    @DeleteMapping("/{videoId}")
    fun deleteVideo(@PathVariable videoId: String) {
        service.deleteVideoById(videoId)
    }
}
