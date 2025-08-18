package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBePresent
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultVideoCrewServiceTest(
    @param:MockK private val mockRepository: VideoCrewRepository,
    @param:MockK private val mockMapper: VideoCrewMapper,
    @param:MockK private val mockVideoService: VideoService,
) {
    @InjectMockKs
    private lateinit var underTest: DefaultVideoCrewService

    @Test
    internal fun `should return all positions sorted`() {
        every { mockRepository.getPositions() } returns setOf("POSITION_2", "POSITION_1")

        val actual = underTest.getPositions()

        actual.shouldContainExactly("POSITION_1", "POSITION_2")
    }

    @Test
    internal fun `should add position to video`() {
        every { mockMapper.modelToEntity(VIDEO_CREW) } returns VIDEO_CREW_ENTITY
        every { mockRepository.save(VIDEO_CREW_ENTITY) } returns VIDEO_CREW_ENTITY
        every { VIDEO_CREW.videoId } returns VIDEO_ID
        every { mockVideoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        val actual = underTest.addPosition(VIDEO_CREW)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should remove position from video`() {
        every { mockMapper.modelToId(VIDEO_CREW) } returns VIDEO_CREW_ENTITY_ID
        every { mockRepository.deleteById(VIDEO_CREW_ENTITY_ID) } returns Unit
        every { VIDEO_CREW.videoId } returns VIDEO_ID
        every { mockVideoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_OTHER)

        val actual = underTest.removePosition(VIDEO_CREW)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO_OTHER }
    }

    private companion object {
        private val VIDEO_ID = mockk<UUID>()
        private val VIDEO_CREW = mockk<VideoCrewRequest>()
        private val VIDEO_CREW_ENTITY_ID = mockk<VideoCrewEntityId>()
        private val VIDEO_CREW_ENTITY = mockk<VideoCrewEntity>()
        private val DETAILED_VIDEO = mockk<DetailedVideo>()
        private val DETAILED_VIDEO_OTHER = mockk<DetailedVideo>()
    }
}
