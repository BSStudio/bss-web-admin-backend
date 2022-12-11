package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import java.util.*

interface VideoCrewService {

    fun getPositions(): List<String>
    fun addPosition(videoCrew: VideoCrewRequest): Optional<DetailedVideo>
    fun removePosition(videoCrew: VideoCrewRequest): Optional<DetailedVideo>
}
