package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.FileUpdate
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

class FileUpdatingVideoService(private val service: VideoService, private val fileClient: FileApiClient) : VideoService {
    override fun findAllVideos(): List<Video> {
        return this.service.findAllVideos()
    }

    override fun findAllVideos(pageable: Pageable): Page<Video> {
        return this.service.findAllVideos(pageable)
    }

    override fun insertVideo(createVideo: CreateVideo): Video {
        return this.service.insertVideo(createVideo)
            .also { this.fileClient.createVideoFolder(FileUpdate(it.id, it.url)) }
    }

    override fun changeVideoVisibility(videoIds: List<UUID>, visible: Boolean): List<UUID> {
        return this.service.changeVideoVisibility(videoIds, visible)
    }

    override fun findVideoById(videoId: UUID): Optional<DetailedVideo> {
        return this.service.findVideoById(videoId)
    }

    override fun updateVideo(videoId: UUID, updateVideo: UpdateVideo): Optional<DetailedVideo> {
        return this.service.updateVideo(videoId, updateVideo)
            .map { this.fileClient.updateVideoFolder(FileUpdate(it.id, it.url)) ; it }
    }

    override fun deleteVideoById(videoId: UUID) {
        return this.service.deleteVideoById(videoId)
    }
}
