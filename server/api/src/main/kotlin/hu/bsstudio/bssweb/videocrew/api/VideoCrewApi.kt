package hu.bsstudio.bssweb.videocrew.api

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/v1/videoCrew")
interface VideoCrewApi {

    @GetMapping("/position")
    fun getPositions(): ResponseEntity<List<String>>

    @PutMapping
    fun addPosition(@ModelAttribute crew: VideoCrewRequest): ResponseEntity<DetailedVideo>

    @DeleteMapping
    fun removePosition(@ModelAttribute crew: VideoCrewRequest): ResponseEntity<DetailedVideo>
}
