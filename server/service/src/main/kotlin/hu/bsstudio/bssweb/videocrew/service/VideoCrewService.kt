package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import java.util.Optional

interface VideoCrewService {

    fun getPositions(): List<String>
    fun addPosition(videoCrew: VideoCrew): Optional<DetailedVideo>
    fun removePosition(videoCrew: VideoCrew): Optional<DetailedVideo>
}
