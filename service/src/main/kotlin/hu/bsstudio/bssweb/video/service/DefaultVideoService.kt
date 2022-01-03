package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.mapper.DetailedVideoMapper
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.VideoRepository
import org.springframework.data.domain.PageRequest
import java.util.Optional
import java.util.stream.Collectors

class DefaultVideoService(
    private val repository: VideoRepository,
    private val detailedRepository: DetailedVideoRepository
) : VideoService {

    internal var mapper: VideoMapper = VideoMapper()
    internal var detailedMapper: DetailedVideoMapper = DetailedVideoMapper()

    override fun findAllVideos(page: Int, size: Int): List<Video> {
        return repository.findAll(PageRequest.of(page, size))
            .get()
            .map(mapper::entityToModel)
            .collect(Collectors.toList())
    }

    override fun insertVideo(createVideo: CreateVideo): Video {
        return VideoEntity(id = createVideo.id, title = createVideo.title)
            .let(repository::save)
            .let(mapper::entityToModel)
    }

    override fun archiveVideos(videoIds: List<String>, unArchive: Boolean): List<String> {
        return repository.findAllById(videoIds)
            .map { it.copy(archived = !unArchive) }
            .map(repository::save)
            .map(VideoEntity::id)
    }

    override fun changeVideoVisibility(videoIds: List<String>, visible: Boolean): List<String> {
        return detailedRepository.findAllById(videoIds)
            .map { it.copy(visible = visible) }
            .map(detailedRepository::save)
            .map(DetailedVideoEntity::id)
    }

    override fun findVideoById(videoId: String): Optional<DetailedVideo> {
        return detailedRepository.findById(videoId)
            .map(detailedMapper::entityToModel)
    }

    override fun updateVideo(videoId: String, updateVideo: UpdateVideo): Optional<DetailedVideo> {
        return detailedRepository.findById(videoId)
            .map { updateVideo(it, updateVideo) }
            .map(detailedRepository::save)
            .map(detailedMapper::entityToModel)
    }

    override fun deleteVideoById(videoId: String) {
        repository.deleteById(videoId)
    }

    private fun updateVideo(videoEntity: DetailedVideoEntity, updateVideo: UpdateVideo): DetailedVideoEntity {
        return videoEntity.copy(
            title = updateVideo.title,
            description = updateVideo.description,
            videoUrl = updateVideo.videoUrl,
            thumbnailUrl = updateVideo.thumbnailUrl,
            uploadedAt = updateVideo.uploadedAt,
            visible = updateVideo.visible,
            archived = updateVideo.archived,
        )
    }
}
