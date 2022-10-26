package hu.bsstudio.bssweb.videocrew.controller

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/videoCrew")
class VideoCrewController(private val service: VideoCrewService) {

    @GetMapping("/position")
    fun getPositions(): ResponseEntity<List<String>> {
        return service.getPositions()
            .let { ResponseEntity.ok(it) }
    }

    @PutMapping
    fun addPosition(@RequestParam videoId: UUID, @RequestParam position: String, @RequestParam memberId: UUID): ResponseEntity<DetailedVideo> {
        return VideoCrew(videoId, position, memberId)
            .let(service::addPosition)
            .let { ResponseEntity.of(it) }
    }

    @DeleteMapping
    fun removePosition(@RequestParam videoId: UUID, @RequestParam position: String, @RequestParam memberId: UUID): ResponseEntity<DetailedVideo> {
        return VideoCrew(videoId, position, memberId)
            .let(service::removePosition)
            .let { ResponseEntity.of(it) }
    }
}
