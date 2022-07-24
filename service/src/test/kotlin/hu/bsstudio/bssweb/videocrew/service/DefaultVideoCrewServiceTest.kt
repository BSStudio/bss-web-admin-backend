package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
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
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultVideoCrewServiceTest {

    @MockK
    private lateinit var mockRepository: VideoCrewRepository
    @MockK
    private lateinit var mockMapper: VideoCrewMapper
    @InjectMockKs
    private lateinit var underTest: DefaultVideoCrewService

    @Test
    internal fun `should add position to video`() {
        every { mockMapper.modelToEntity(VIDEO_CREW) } returns VIDEO_CREW_ENTITY_1
        every { mockMapper.entityToModel(VIDEO_CREW_ENTITY_1) } returns SIMPLE_VIDEO_CREW_1
        every { mockMapper.entityToModel(VIDEO_CREW_ENTITY_2) } returns SIMPLE_VIDEO_CREW_2
        every { mockRepository.save(VIDEO_CREW_ENTITY_1) } returns VIDEO_CREW_ENTITY_1
        every { mockRepository.findAllByVideoId(VIDEO_ID) } returns listOf(VIDEO_CREW_ENTITY_1, VIDEO_CREW_ENTITY_2)

        val result = underTest.addPosition(VIDEO_CREW)

        assertThat(result).isEqualTo(listOf(SIMPLE_VIDEO_CREW_1, SIMPLE_VIDEO_CREW_2))
    }

    @Test
    internal fun `should remove position from video`() {
        every { mockMapper.modelToEntity(VIDEO_CREW) } returns VIDEO_CREW_ENTITY_1
        every { mockMapper.entityToModel(VIDEO_CREW_ENTITY_2) } returns SIMPLE_VIDEO_CREW_2
        every { mockRepository.deleteById(VIDEO_CREW_ENTITY_1) } returns Unit
        every { mockRepository.findAllByVideoId(VIDEO_ID) } returns listOf(VIDEO_CREW_ENTITY_2)

        val result = underTest.removePosition(VIDEO_CREW)

        assertThat(result).isEqualTo(listOf(SIMPLE_VIDEO_CREW_2))
    }

    private companion object {
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val POSITION_1 = "position1"
        private const val POSITION_2 = "position2"
        private val MEMBER_ID_1 = UUID.fromString("11234567-0123-0123-0123-0123456789ab")
        private val MEMBER_ID_2 = UUID.fromString("21234567-0123-0123-0123-0123456789ab")
        private val VIDEO_CREW = VideoCrew(VIDEO_ID, POSITION_1, MEMBER_ID_1)
        private val SIMPLE_VIDEO_CREW_1 = SimpleCrew(POSITION_1, MEMBER_ID_1)
        private val SIMPLE_VIDEO_CREW_2 = SimpleCrew(POSITION_2, MEMBER_ID_2)
        private val VIDEO_CREW_ENTITY_1 = VideoCrewEntity(VIDEO_ID, POSITION_1, MEMBER_ID_1)
        private val VIDEO_CREW_ENTITY_2 = VideoCrewEntity(VIDEO_ID, POSITION_2, MEMBER_ID_2)
    }
}
