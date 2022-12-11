package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultVideoCrewServiceTest {

    @MockK
    private lateinit var mockRepository: VideoCrewRepository
    @MockK
    private lateinit var mockMapper: VideoCrewMapper
    @MockK
    private lateinit var mockVideoService: VideoService
    @InjectMockKs
    private lateinit var underTest: DefaultVideoCrewService

    @Test
    internal fun `should return all positions sorted`() {
        every { mockRepository.getPositions() } returns setOf("POSITION_2", "POSITION_1")

        val result = underTest.getPositions()

        assertThat(result).isEqualTo(listOf("POSITION_1", "POSITION_2"))
    }

    @Test
    internal fun `should add position to video`() {
        every { mockMapper.modelToEntity(VIDEO_CREW) } returns VIDEO_CREW_ENTITY
        every { mockRepository.save(VIDEO_CREW_ENTITY) } returns VIDEO_CREW_ENTITY
        every { VIDEO_CREW.videoId } returns VIDEO_ID
        every { mockVideoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        val result = underTest.addPosition(VIDEO_CREW)

        assertThat(result).hasValue(DETAILED_VIDEO)
    }

    @Test
    internal fun `should remove position from video`() {
        every { mockMapper.modelToId(VIDEO_CREW) } returns VIDEO_CREW_ENTITY_ID
        every { mockRepository.deleteById(VIDEO_CREW_ENTITY_ID) } returns Unit
        every { VIDEO_CREW.videoId } returns VIDEO_ID
        every { mockVideoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_OTHER)

        val result = underTest.removePosition(VIDEO_CREW)

        assertThat(result).hasValue(DETAILED_VIDEO_OTHER)
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
