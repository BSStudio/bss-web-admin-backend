package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.revision.mapper.RevisionMapper
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.repository.VideoRepository
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
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
import org.springframework.data.history.Revision
import org.springframework.data.history.RevisionMetadata
import org.springframework.data.history.Revisions
import java.time.Instant
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultVideoServiceTest(
    @param:MockK private val mockRepository: VideoRepository,
    @param:MockK private val mockMapper: VideoMapper,
    @param:MockK private val mockLabelRepository: LabelRepository,
    @param:MockK private val mockRevisionMapper: RevisionMapper,
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
        every { mockRepository.findById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_ENTITY)
        every { mockMapper.entityToDetailedModel(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO

        val actual = underTest.findVideoById(VIDEO_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should return empty if video was not found`() {
        every { mockRepository.findById(VIDEO_ID) } returns Optional.empty()

        val actual = underTest.findVideoById(VIDEO_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should not update video if id was not found`() {
        every { UPDATE_VIDEO.labels } returns LABELS
        every { mockLabelRepository.findAllByNameIn(LABELS) } returns LABEL_ENTITIES
        every { mockRepository.findById(VIDEO_ID) } returns Optional.empty()

        val actual = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should update video`() {
        every { UPDATE_VIDEO.labels } returns LABELS
        every { mockLabelRepository.findAllByNameIn(LABELS) } returns LABEL_ENTITIES
        every { mockRepository.findById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_ENTITY)
        every { mockMapper.updateToEntity(DETAILED_VIDEO_ENTITY, UPDATE_VIDEO, LABEL_ENTITIES) } returns DETAILED_VIDEO_ENTITY
        every { mockRepository.save(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO_ENTITY
        every { mockMapper.entityToDetailedModel(DETAILED_VIDEO_ENTITY) } returns DETAILED_VIDEO

        val actual = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should mark the entity deleted`() {
        val entity = VideoEntity(title = "title").apply { id = UUID.randomUUID() }
        every { mockRepository.findById(VIDEO_ID) } returns Optional.of(entity)
        every { mockRepository.save(entity) } returns entity

        underTest.deleteVideoById(VIDEO_ID)

        entity.deletedAt.shouldNotBeNull()
    }

    @Test
    internal fun `should do nothing when deleting a missing entity`() {
        every { mockRepository.findById(VIDEO_ID) } returns Optional.empty()

        underTest.deleteVideoById(VIDEO_ID)
    }

    @Test
    internal fun `should return deleted videos`() {
        every { mockRepository.findAllDeleted(PAGEABLE) } returns PageImpl(listOf(VIDEO_ENTITY))
        every { mockMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO

        val actual = underTest.findDeletedVideos(PAGEABLE)

        actual shouldBeEqual PageImpl(listOf(VIDEO))
    }

    @Test
    internal fun `should restore a deleted video`() {
        val entity =
            VideoEntity(title = "title").apply {
                id = UUID.randomUUID()
                deletedAt = Instant.now()
            }
        every { mockRepository.findDeletedById(VIDEO_ID) } returns Optional.of(entity)
        every { mockRepository.save(entity) } returns entity
        every { mockMapper.entityToDetailedModel(entity) } returns DETAILED_VIDEO

        val actual = underTest.restoreVideo(VIDEO_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
        entity.deletedAt.shouldBeNull()
    }

    @Test
    internal fun `should not restore a video that is not deleted`() {
        every { mockRepository.findDeletedById(VIDEO_ID) } returns Optional.empty()

        val actual = underTest.restoreVideo(VIDEO_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should return the revisions of a video`() {
        val revisions = Revisions.none<Long, VideoEntity>()
        every { mockRepository.findRevisions(VIDEO_ID) } returns revisions
        every { mockRevisionMapper.revisionsToModel(revisions, any<(VideoEntity) -> Instant?>()) } returns REVISION_INFOS

        val actual = underTest.findVideoRevisions(VIDEO_ID)

        actual shouldBeEqual REVISION_INFOS
    }

    @Test
    internal fun `should return a video as it was at a revision`() {
        every { mockRepository.findRevision(VIDEO_ID, REVISION) } returns Optional.of(Revision.of(REVISION_METADATA, VIDEO_ENTITY))
        every { mockMapper.revisionToModel(VIDEO_ENTITY) } returns DETAILED_VIDEO

        val actual = underTest.findVideoRevision(VIDEO_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should give a video the fields it had at a revision`() {
        val current = VideoEntity(title = "title").apply { id = UUID.randomUUID() }
        every { mockRepository.findRevision(VIDEO_ID, REVISION) } returns Optional.of(Revision.of(REVISION_METADATA, VIDEO_ENTITY))
        every { mockMapper.entityToUpdate(VIDEO_ENTITY) } returns UPDATE_VIDEO
        every { UPDATE_VIDEO.labels } returns LABELS
        every { mockLabelRepository.findAllByNameIn(LABELS) } returns LABEL_ENTITIES
        every { mockMapper.updateToEntity(current, UPDATE_VIDEO, LABEL_ENTITIES) } returns current
        every { mockRepository.findById(VIDEO_ID) } returns Optional.of(current)
        every { mockRepository.save(current) } returns current
        every { mockMapper.entityToDetailedModel(current) } returns DETAILED_VIDEO

        val actual = underTest.restoreVideoRevision(VIDEO_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should not restore a revision that is missing`() {
        every { mockRepository.findRevision(VIDEO_ID, REVISION) } returns Optional.empty()

        val actual = underTest.restoreVideoRevision(VIDEO_ID, REVISION)

        actual.shouldBeEmpty()
    }

    private companion object {
        private const val REVISION = 3L
        private val REVISION_METADATA = mockk<RevisionMetadata<Long>>()
        private val REVISION_INFOS = listOf(mockk<RevisionInfo>())
        private val PAGEABLE = Pageable.unpaged()
        private val VIDEO_ID = mockk<UUID>()
        private val VIDEO_ENTITY = mockk<VideoEntity>()
        private val VIDEO_ENTITY_LIST = listOf(VIDEO_ENTITY)
        private val VIDEO = mockk<Video>()
        private val PAGED_VIDEOS = PageImpl(listOf(VIDEO))
        private val CREATE_VIDEO = mockk<CreateVideo>()
        private val DETAILED_VIDEO_ENTITY = mockk<VideoEntity>()
        private val DETAILED_VIDEO = mockk<DetailedVideo>()
        private val UPDATE_VIDEO = mockk<UpdateVideo>()
        private val LABELS = mockk<List<String>>()
        private val LABEL_ENTITIES = mockk<List<LabelEntity>>()
    }
}
