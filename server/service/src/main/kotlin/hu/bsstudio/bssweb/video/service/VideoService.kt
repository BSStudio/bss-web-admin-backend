package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.revision.model.RevisionInfo
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

    fun changeVideoVisibility(
        videoIds: List<UUID>,
        visible: Boolean,
    ): List<UUID>

    fun findVideoById(videoId: UUID): Optional<DetailedVideo>

    fun updateVideo(
        videoId: UUID,
        updateVideo: UpdateVideo,
    ): Optional<DetailedVideo>

    fun deleteVideoById(videoId: UUID)

    fun findDeletedVideos(pageable: Pageable): Page<Video>

    /** Brings back a deleted video, or returns empty if no deleted video has the id. */
    fun restoreVideo(videoId: UUID): Optional<DetailedVideo>

    /** The changes of the video, newest first, including the ones made while it was deleted. */
    fun findVideoRevisions(videoId: UUID): List<RevisionInfo>

    /** The video as it was at [revision], without its crew, which has no history. */
    fun findVideoRevision(
        videoId: UUID,
        revision: Long,
    ): Optional<DetailedVideo>

    /** Gives the video the fields and labels it had at [revision]. A deleted video has to be restored first. */
    fun restoreVideoRevision(
        videoId: UUID,
        revision: Long,
    ): Optional<DetailedVideo>
}
