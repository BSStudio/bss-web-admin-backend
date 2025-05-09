package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.VideoFileUpdate
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

class FileUpdatingVideoService(
    private val service: VideoService,
    private val fileClient: FileApiClient,
) : VideoService {
    override fun findAllVideos(): List<Video> = this.service.findAllVideos()

    override fun findAllVideos(pageable: Pageable): Page<Video> = this.service.findAllVideos(pageable)

    override fun insertVideo(createVideo: CreateVideo): Video =
        this.service
            .insertVideo(createVideo)
            .also { this.fileClient.createVideoFolder(VideoFileUpdate(it.id, it.urls)) }

    override fun changeVideoVisibility(
        videoIds: List<UUID>,
        visible: Boolean,
    ): List<UUID> = this.service.changeVideoVisibility(videoIds, visible)

    override fun findVideoById(videoId: UUID): Optional<DetailedVideo> = this.service.findVideoById(videoId)

    override fun updateVideo(
        videoId: UUID,
        updateVideo: UpdateVideo,
    ): Optional<DetailedVideo> =
        this.service
            .updateVideo(videoId, updateVideo)
            .map {
                this.fileClient.updateVideoFolder(VideoFileUpdate(it.id, it.urls))
                it
            }

    override fun deleteVideoById(videoId: UUID) = this.service.deleteVideoById(videoId)
}
