package hu.bsstudio.bssweb.videocrew.controller

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.operation.VideoCrewOperation
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class VideoCrewController(
    private val service: VideoCrewService,
) : VideoCrewOperation {
    override fun getPositions(): ResponseEntity<List<String>> = service.getPositions().let { ResponseEntity.ok(it) }

    override fun addPosition(
        videoId: UUID,
        position: String,
        memberId: UUID,
    ): ResponseEntity<DetailedVideo> = service.addPosition(VideoCrewRequest(videoId, position, memberId)).let { ResponseEntity.of(it) }

    override fun removePosition(
        videoId: UUID,
        position: String,
        memberId: UUID,
    ): ResponseEntity<DetailedVideo> = service.removePosition(VideoCrewRequest(videoId, position, memberId)).let { ResponseEntity.of(it) }
}
