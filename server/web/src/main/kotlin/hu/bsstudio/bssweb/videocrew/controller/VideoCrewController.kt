package hu.bsstudio.bssweb.videocrew.controller

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.api.VideoCrewApi
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class VideoCrewController(private val service: VideoCrewService) : VideoCrewApi {

    override fun getPositions(): ResponseEntity<List<String>> {
        return service.getPositions().let { ResponseEntity.ok(it) }
    }

    override fun addPosition(crew: VideoCrewRequest): ResponseEntity<DetailedVideo> {
        return service.addPosition(crew).let { ResponseEntity.of(it) }
    }

    override fun removePosition(crew: VideoCrewRequest): ResponseEntity<DetailedVideo> {
        return service.removePosition(crew).let { ResponseEntity.of(it) }
    }
}
