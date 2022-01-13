package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.VideoRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDate
import java.util.Optional

internal class DefaultVideoServiceTest {

    @MockK
    private lateinit var mockRepository: VideoRepository

    @MockK
    private lateinit var mockDetailedRepository: DetailedVideoRepository

    @MockK
    private lateinit var mockMapper: VideoMapper

    private lateinit var underTest: DefaultVideoService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultVideoService(mockRepository, mockDetailedRepository, mockMapper)
    }

    @Test
    internal fun `should return all videos`() {
        val page = 0
        val size = 10

        every { mockRepository.findAll(PageRequest.of(0, 10)) } returns PageImpl(videoEntityList)
        every { mockMapper.entityToModel(videoEntity1) } returns video1

        val response = underTest.findAllVideos(page, size)

        Assertions.assertThat(response).isEqualTo(videoList)
    }

    @Test
    internal fun `should insert new video`() {
        every { mockMapper.modelToEntity(createVideo) } returns videoEntity1
        every { mockRepository.save(videoEntity1) } returns videoEntity1
        every { mockMapper.entityToModel(videoEntity1) } returns video1

        val response = underTest.insertVideo(createVideo)

        Assertions.assertThat(response).isEqualTo(video1)
    }

    @Test
    internal fun `should archive video`() {
        val videoIds = listOf(videoId1)
        every { mockRepository.findAllById(videoIds) } returns listOf(videoEntity1)
        val updatedVideoEntity = videoEntity1.copy(archived = true)
        every { mockRepository.save(updatedVideoEntity) } returns updatedVideoEntity
        every { mockMapper.entityToModel(updatedVideoEntity) } returns video1

        val response = underTest.archiveVideos(videoIds)

        Assertions.assertThat(response).isEqualTo(listOf(videoId1))
    }

    @Test
    internal fun `should archive video with implicit archive flag`() {
        val videoIds = listOf(videoId1)
        every { mockRepository.findAllById(videoIds) } returns listOf(videoEntity1)
        val updatedVideoEntity = videoEntity1.copy(archived = true)
        every { mockRepository.save(updatedVideoEntity) } returns updatedVideoEntity
        every { mockMapper.entityToModel(updatedVideoEntity) } returns video1

        val response = underTest.archiveVideos(videoIds, true)

        Assertions.assertThat(response).isEqualTo(listOf(videoId1))
    }

    @Test
    internal fun `should change video visibility`() {
        val videoIds = listOf(videoId1)
        every { mockRepository.findAllById(videoIds) } returns listOf(videoEntity1)
        val updatedVideoEntity = videoEntity1.copy(visible = false)
        every { mockRepository.save(updatedVideoEntity) } returns updatedVideoEntity
        every { mockMapper.entityToModel(updatedVideoEntity) } returns video1

        val response = underTest.changeVideoVisibility(videoIds, false)

        Assertions.assertThat(response).isEqualTo(listOf(videoId1))
    }

    @Test
    internal fun `should find video by id`() {
        every { mockDetailedRepository.findById(videoId1) } returns Optional.of(detailedVideoEntity1)
        every { mockMapper.entityToModel(detailedVideoEntity1) } returns detailedVideo1

        val response = underTest.findVideoById(videoId1)

        Assertions.assertThat(response).isEqualTo(Optional.of(detailedVideo1))
    }

    @Test
    internal fun `should return empty if video was not found`() {
        every { mockDetailedRepository.findById(videoId1) } returns Optional.empty()

        val response = underTest.findVideoById(videoId1)

        Assertions.assertThat(response).isEqualTo(Optional.empty<Video>())
    }

    @Test
    internal fun `should not update video if id was not found`() {
        every { mockDetailedRepository.findById(videoId1) } returns Optional.empty()

        val response = underTest.updateVideo(videoId1, updateVideo)

        Assertions.assertThat(response).isEqualTo(Optional.empty<Video>())
    }

    @Test
    internal fun `should update video`() {
        every { mockDetailedRepository.findById(videoId1) } returns Optional.of(detailedVideoEntity1)
        val updatedDetailedEntity = detailedVideoEntity1.copy(
            title = updateVideo.title,
            description = updateVideo.description,
            videoUrl = updateVideo.videoUrl,
            thumbnailUrl = updateVideo.thumbnailUrl,
            uploadedAt = updateVideo.uploadedAt,
            visible = updateVideo.visible,
            archived = updateVideo.archived,
        )
        every { mockDetailedRepository.save(updatedDetailedEntity) } returns updatedDetailedEntity
        val updatedVideo = detailedVideo1.copy(
            title = updateVideo.title,
            description = updateVideo.description,
            videoUrl = updateVideo.videoUrl,
            thumbnailUrl = updateVideo.thumbnailUrl,
            uploadedAt = updateVideo.uploadedAt,
            visible = updateVideo.visible,
            archived = updateVideo.archived,
        )
        every { mockMapper.entityToModel(updatedDetailedEntity) } returns updatedVideo

        val response = underTest.updateVideo(videoId1, updateVideo)

        Assertions.assertThat(response).isEqualTo(Optional.of(updatedVideo))
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(videoId1) } returns Unit

        underTest.deleteVideoById(videoId1)
    }

    private companion object {
        private const val videoId1 = "videoId1"
        private val videoEntity1 = VideoEntity(videoId1, "title", LocalDate.of(2022, 1, 1), visible = true, archived = false)
        private val videoEntityList = listOf(videoEntity1)
        private val video1 = Video(videoId1, "title", LocalDate.of(2022, 1, 1), visible = true, archived = false)
        private val videoList = listOf(video1)
        private val createVideo = CreateVideo(videoId1, "title")
        private val detailedVideoEntity1 = DetailedVideoEntity(videoId1, "title", "description", "videoUrl", "thumbnailUrl", LocalDate.of(2022, 1, 1), visible = true, archived = false, videoCrew = listOf())
        private val detailedVideo1 = DetailedVideo(videoId1, "title", "description", "videoUrl", "thumbnailUrl", LocalDate.of(2022, 1, 1), visible = true, archived = false, crew = listOf())
        private val updateVideo = UpdateVideo("updatedTitle", "updatedDescription", "updatedVideoUrl", "updatedThumbnailUrl", LocalDate.of(2022, 2, 2), visible = false, archived = true)
    }
}
