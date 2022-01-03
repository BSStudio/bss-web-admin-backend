package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew

interface VideoCrewService {
    fun addPosition(videoCrew: VideoCrew): List<SimpleCrew>
    fun removePosition(videoCrew: VideoCrew): List<SimpleCrew>
}
