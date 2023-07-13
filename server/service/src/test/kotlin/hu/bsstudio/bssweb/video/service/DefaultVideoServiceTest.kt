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
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultVideoServiceTest(
    @MockK private val mockRepository: VideoRepository,
    @MockK private val mockDetailedRepository: DetailedVideoRepository,
    @MockK private val mockMapper: VideoMapper
) {

    @InjectMockKs
    private lateinit var underTest: DefaultVideoService

    @Test
    internal fun `should return all videos`() {
        every { mockRepository.findAll() } returns listOf(VIDEO_ENTITY)
        every { mockMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO

        val response = underTest.findAllVideos()

        assertThat(response).isEqualTo(listOf(VIDEO))
    }

    @Test
    internal fun `should return all videos paged`() {
        every { mockRepository.findAll(PAGEABLE) } returns PageImpl(VIDEO_ENTITY_LIST)
        every { mockMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO

        val response = underTest.findAllVideos(PAGEABLE)

        assertThat(response).isEqualTo(PAGED_VIDEOS)
    }

    @Test
    internal fun `should insert new video`() {
        every { mockMapper.modelToEntity(CREATE_VIDEO) } returns VIDEO_ENTITY
        every { mockRepository.save(VIDEO_ENTITY) } returns VIDEO_ENTITY
        every { mockMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO

        val response = underTest.insertVideo(CREATE_VIDEO)

        assertThat(response).isEqualTo(VIDEO)
    }

    @Test
    internal fun `should change video visibility`() {
        val videoIds = listOf(VIDEO_ID)
        every { mockRepository.findAllById(videoIds) } returns listOf(VIDEO_ENTITY)
        val updatedVideoEntity = VIDEO_ENTITY.copy(visible = false)
        every { mockRepository.save(updatedVideoEntity) } returns updatedVideoEntity
        every { mockMapper.entityToModel(updatedVideoEntity) } returns VIDEO

        val response = underTest.changeVideoVisibility(videoIds, false)

        assertThat(response).isEqualTo(listOf(VIDEO_ID))
    }

    @Test
    internal fun `should find video by id`() {
        every { mockDetailedRepository.findById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_ENTITY)
        every { mockMapper.entityToModel(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO

        val response = underTest.findVideoById(VIDEO_ID)

        assertThat(response).hasValue(DETAILED_VIDEO)
    }

    @Test
    internal fun `should return empty if video was not found`() {
        every { mockDetailedRepository.findById(VIDEO_ID) } returns Optional.empty()

        val response = underTest.findVideoById(VIDEO_ID)

        assertThat(response).isEmpty
    }

    @Test
    internal fun `should not update video if id was not found`() {
        every { mockDetailedRepository.findById(VIDEO_ID) } returns Optional.empty()

        val response = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        assertThat(response).isEmpty
    }

    @Test
    internal fun `should update video`() {
        every { mockDetailedRepository.findById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_ENTITY)
        val updatedDetailedEntity = DETAILED_VIDEO_ENTITY.copy(
            url = UPDATE_VIDEO.url,
            title = UPDATE_VIDEO.title,
            description = UPDATE_VIDEO.description,
            uploadedAt = UPDATE_VIDEO.uploadedAt,
            visible = UPDATE_VIDEO.visible
        )
        every { mockDetailedRepository.save(updatedDetailedEntity) } returns updatedDetailedEntity
        val updatedVideo = DETAILED_VIDEO.copy(
            url = UPDATE_VIDEO.url,
            title = UPDATE_VIDEO.title,
            description = UPDATE_VIDEO.description,
            uploadedAt = UPDATE_VIDEO.uploadedAt,
            visible = UPDATE_VIDEO.visible
        )
        every { mockMapper.entityToModel(updatedDetailedEntity) } returns updatedVideo

        val response = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        assertThat(response).hasValue(updatedVideo)
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(VIDEO_ID) } returns Unit

        underTest.deleteVideoById(VIDEO_ID)
    }

    private companion object {
        private val PAGEABLE = Pageable.unpaged()
        private val VIDEO_ID = mockk<UUID>()
        private val VIDEO_ENTITY = VideoEntity(VIDEO_ID, "url", "title", LocalDate.of(2022, 1, 1), visible = true)
        private val VIDEO_ENTITY_LIST = listOf(VIDEO_ENTITY)
        private val VIDEO = Video(VIDEO_ID, "url", "title", LocalDate.of(2022, 1, 1), visible = true)
        private val PAGED_VIDEOS = PageImpl(listOf(VIDEO))
        private val CREATE_VIDEO = CreateVideo("url", "title")
        private val DETAILED_VIDEO_ENTITY = DetailedVideoEntity(VIDEO_ID, "url", "title", "description", LocalDate.of(2022, 1, 1), visible = true, videoCrew = listOf())
        private val DETAILED_VIDEO = DetailedVideo(VIDEO_ID, "url", "title", "description", LocalDate.of(2022, 1, 1), visible = true, crew = listOf())
        private val UPDATE_VIDEO = UpdateVideo("updatedUrl", "updatedTitle", "updatedDescription", LocalDate.of(2022, 2, 2), visible = false)
    }
}
