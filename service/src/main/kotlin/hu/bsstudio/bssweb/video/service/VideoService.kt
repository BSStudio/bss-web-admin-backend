package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import java.util.Optional

interface VideoService {
    fun findAllVideos(page: Int, size: Int): List<Video>
    fun insertVideo(createVideo: CreateVideo): Video
    fun archiveVideos(videoIds: List<String>, unArchive: Boolean): List<String>
    fun changeVideoVisibility(videoIds: List<String>, visible: Boolean): List<String>
    fun findVideoById(videoId: String): Optional<DetailedVideo>
    fun updateVideo(videoId: String, updateVideo: UpdateVideo): Optional<DetailedVideo>
    fun deleteVideoById(videoId: String)
}
