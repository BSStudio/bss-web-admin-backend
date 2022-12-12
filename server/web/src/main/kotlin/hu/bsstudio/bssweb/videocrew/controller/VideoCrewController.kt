package hu.bsstudio.bssweb.videocrew.controller

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/videoCrew")
class VideoCrewController(private val service: VideoCrewService) {

    @GetMapping("/position")
    fun getPositions(): ResponseEntity<List<String>> {
        return service.getPositions().let { ResponseEntity.ok(it) }
    }

    @PutMapping
    fun addPosition(@ModelAttribute crew: VideoCrewRequest): ResponseEntity<DetailedVideo> {
        return service.addPosition(crew).let { ResponseEntity.of(it) }
    }

    @DeleteMapping
    fun removePosition(@ModelAttribute crew: VideoCrewRequest): ResponseEntity<DetailedVideo> {
        return service.removePosition(crew).let { ResponseEntity.of(it) }
    }
}
