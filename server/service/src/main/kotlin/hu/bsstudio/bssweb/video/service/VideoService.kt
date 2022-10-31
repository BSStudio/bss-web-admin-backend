package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface VideoService {
    fun findAllVideos(): List<Video>
    fun findAllVideos(pageable: Pageable): Page<Video>
    fun insertVideo(createVideo: CreateVideo): Video
    fun changeVideoVisibility(videoIds: List<UUID>, visible: Boolean): List<UUID>
    fun findVideoById(videoId: UUID): Optional<DetailedVideo>
    fun updateVideo(videoId: UUID, updateVideo: UpdateVideo): Optional<DetailedVideo>
    fun deleteVideoById(videoId: UUID)
}
