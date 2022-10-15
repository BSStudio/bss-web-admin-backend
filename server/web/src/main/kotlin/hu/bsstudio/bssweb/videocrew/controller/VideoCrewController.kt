package hu.bsstudio.bssweb.videocrew.controller

import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/videoCrew")
class VideoCrewController(private val service: VideoCrewService) {

    @PostMapping
    fun addPosition(@RequestParam videoId: UUID, @RequestParam position: String, @RequestParam memberId: UUID): ResponseEntity<List<SimpleCrew>> {
        return VideoCrew(videoId, position, memberId)
            .let(service::addPosition)
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @DeleteMapping
    fun removePosition(@RequestParam videoId: UUID, @RequestParam position: String, @RequestParam memberId: UUID): ResponseEntity<List<SimpleCrew>> {
        return VideoCrew(videoId, position, memberId)
            .let(service::removePosition)
            .let { ResponseEntity.ok(it) }
    }
}
