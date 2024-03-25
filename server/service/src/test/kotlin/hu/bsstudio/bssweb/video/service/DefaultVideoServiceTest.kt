package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultVideoServiceTest(
    @MockK private val mockRepository: SimpleVideoRepository,
    @MockK private val mockDetailedRepository: DetailedVideoRepository,
    @MockK private val mockMapper: VideoMapper,
    @MockK private val mockLabelRepository: LabelRepository,
) {
    @InjectMockKs
    private lateinit var underTest: DefaultVideoService

    @Test
    internal fun `should return all videos`() {
        every { mockRepository.findAll() } returns listOf(VIDEO_ENTITY)
        every { mockMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO

        val actual = underTest.findAllVideos()

        actual.shouldContainExactly(VIDEO)
    }

    @Test
    internal fun `should return all videos paged`() {
        every { mockRepository.findAll(PAGEABLE) } returns PageImpl(VIDEO_ENTITY_LIST)
        every { mockMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO

        val actual = underTest.findAllVideos(PAGEABLE)

        actual shouldBeEqual PAGED_VIDEOS
    }

    @Test
    internal fun `should insert new video`() {
        every { mockMapper.modelToEntity(CREATE_VIDEO) } returns VIDEO_ENTITY
        every { mockRepository.save(VIDEO_ENTITY) } returns VIDEO_ENTITY
        every { mockMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO

        val actual = underTest.insertVideo(CREATE_VIDEO)

        actual shouldBeEqual VIDEO
    }

    @Test
    internal fun `should change video visibility`() {
        val videoIds = listOf(VIDEO_ID)
        every { mockRepository.findAllById(videoIds) } returns listOf(VIDEO_ENTITY)
        every { VIDEO_ENTITY.visible = false } returns Unit
        every { mockRepository.save(VIDEO_ENTITY) } returns VIDEO_ENTITY
        every { VIDEO_ENTITY.id } returns VIDEO_ID

        val actual = underTest.changeVideoVisibility(videoIds, false)

        actual.shouldContainExactly(VIDEO_ID)
    }

    @Test
    internal fun `should find video by id`() {
        every { mockDetailedRepository.findById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_ENTITY)
        every { mockMapper.entityToModel(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO

        val actual = underTest.findVideoById(VIDEO_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should return empty if video was not found`() {
        every { mockDetailedRepository.findById(VIDEO_ID) } returns Optional.empty()

        val actual = underTest.findVideoById(VIDEO_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should not update video if id was not found`() {
        every { UPDATE_VIDEO.labels } returns LABELS
        every { mockLabelRepository.findAllByNameIn(LABELS) } returns LABEL_ENTITIES
        every { mockDetailedRepository.findById(VIDEO_ID) } returns Optional.empty()

        val actual = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should update video`() {
        every { UPDATE_VIDEO.labels } returns LABELS
        every { mockLabelRepository.findAllByNameIn(LABELS) } returns LABEL_ENTITIES
        every { mockDetailedRepository.findById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_ENTITY)
        every { mockMapper.updateToEntity(DETAILED_VIDEO_ENTITY, UPDATE_VIDEO, LABEL_ENTITIES) } returns DETAILED_VIDEO_ENTITY
        every { mockDetailedRepository.save(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO_ENTITY
        every { mockMapper.entityToModel(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO

        val actual = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
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
        private val LABELS = mockk<List<String>>()
        private val LABEL_ENTITIES = mockk<List<LabelEntity>>()
    }
}
