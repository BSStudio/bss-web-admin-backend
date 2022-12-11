package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.*

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
        every { mockMapper.modelToEntity(VIDEO_CREW) } returns VIDEO_CREW_ENTITY_1
        every { mockRepository.save(VIDEO_CREW_ENTITY_1) } returns VIDEO_CREW_ENTITY_1
        every { mockVideoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        val result = underTest.addPosition(VIDEO_CREW)

        assertThat(result).hasValue(DETAILED_VIDEO)
    }

    @Test
    internal fun `should remove position from video`() {
        every { mockMapper.modelToId(VIDEO_CREW) } returns VIDEO_CREW_ENTITY_ID_1
        every { mockRepository.deleteById(VIDEO_CREW_ENTITY_ID_1) } returns Unit
        every { mockVideoService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO_EMPTY)

        val result = underTest.removePosition(VIDEO_CREW)

        assertThat(result).hasValue(DETAILED_VIDEO_EMPTY)
    }

    private companion object {
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val POSITION_1 = "position1"
        private val MEMBER_ID_1 = UUID.fromString("11234567-0123-0123-0123-0123456789ab")
        private val VIDEO_CREW = VideoCrew(VIDEO_ID, POSITION_1, MEMBER_ID_1)
        private val VIDEO_CREW_ENTITY_ID_1 = VideoCrewEntityId(VIDEO_ID, POSITION_1, MEMBER_ID_1)
        private val VIDEO_CREW_ENTITY_1 = VideoCrewEntity(id = VIDEO_CREW_ENTITY_ID_1)
        private val SIMPLE_VIDEO_CREW = SimpleCrew(POSITION_1, MEMBER_ID_1)
        private val DETAILED_VIDEO = DetailedVideo(
            id = VIDEO_ID,
            url = "url",
            title = "title",
            description = "description",
            visible = true,
            uploadedAt = LocalDate.of(2022, 1, 1),
            crew = listOf(SIMPLE_VIDEO_CREW)
        )
        private val DETAILED_VIDEO_EMPTY = DETAILED_VIDEO.copy(crew = listOf())
    }
}
