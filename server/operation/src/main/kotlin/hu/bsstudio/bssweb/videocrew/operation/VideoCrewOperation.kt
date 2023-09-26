package hu.bsstudio.bssweb.videocrew.operation

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

interface VideoCrewOperation {

    @GetMapping("/api/v1/videoCrew/position")
    fun getPositions(): ResponseEntity<List<String>>

    @PutMapping("/api/v1/videoCrew")
    fun addPosition(@ModelAttribute crew: VideoCrewRequest): ResponseEntity<DetailedVideo>

    @DeleteMapping("/api/v1/videoCrew")
    fun removePosition(@ModelAttribute crew: VideoCrewRequest): ResponseEntity<DetailedVideo>
}
