package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
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
    @MockK private val mockRepository: SimpleVideoRepository,
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
        every { VIDEO_ENTITY.visible = false } returns Unit
        every { mockRepository.save(VIDEO_ENTITY) } returns VIDEO_ENTITY
        every { VIDEO_ENTITY.id } returns VIDEO_ID

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
        every { UPDATE_VIDEO.url } returns URL
        every { DETAILED_VIDEO_ENTITY.url = URL } returns Unit
        every { UPDATE_VIDEO.title } returns TITLE
        every { DETAILED_VIDEO_ENTITY.title = TITLE } returns Unit
        every { UPDATE_VIDEO.description } returns DESCRIPTION
        every { DETAILED_VIDEO_ENTITY.description = DESCRIPTION } returns Unit
        every { UPDATE_VIDEO.uploadedAt } returns UPLOADED_AT
        every { DETAILED_VIDEO_ENTITY.uploadedAt = UPLOADED_AT } returns Unit
        every { UPDATE_VIDEO.visible } returns VISIBLE
        every { DETAILED_VIDEO_ENTITY.visible = VISIBLE } returns Unit
        every { mockDetailedRepository.save(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO_ENTITY
        every { mockMapper.entityToModel(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO

        val response = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        assertThat(response).hasValue(DETAILED_VIDEO)
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(VIDEO_ID) } returns Unit

        underTest.deleteVideoById(VIDEO_ID)
    }

    private companion object {
        private val PAGEABLE = Pageable.unpaged()
        private val VIDEO_ID = mockk<UUID>()
        private val VIDEO_ENTITY = mockk<SimpleVideoEntity>()
        private val VIDEO_ENTITY_LIST = listOf(VIDEO_ENTITY)
        private val VIDEO = mockk<Video>()
        private val PAGED_VIDEOS = PageImpl(listOf(VIDEO))
        private val CREATE_VIDEO = mockk<CreateVideo>()
        private val DETAILED_VIDEO_ENTITY = mockk<DetailedVideoEntity>()
        private val DETAILED_VIDEO = mockk<DetailedVideo>()
        private val UPDATE_VIDEO = mockk<UpdateVideo>()
        private const val URL = "url"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private val UPLOADED_AT = LocalDate.now()
        private const val VISIBLE = true
    }
}
