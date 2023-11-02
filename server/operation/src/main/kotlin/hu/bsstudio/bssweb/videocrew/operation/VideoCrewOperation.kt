package hu.bsstudio.bssweb.videocrew.operation

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

interface VideoCrewOperation {

    @GetMapping("/api/v1/videoCrew/position")
    fun getPositions(): ResponseEntity<List<String>>

    @PutMapping("/api/v1/videoCrew")
    fun addPosition(@RequestParam videoId: UUID, @RequestParam position: String, @RequestParam memberId: UUID): ResponseEntity<DetailedVideo>

    @DeleteMapping("/api/v1/videoCrew")
    fun removePosition(@RequestParam videoId: UUID, @RequestParam position: String, @RequestParam memberId: UUID): ResponseEntity<DetailedVideo>
}
