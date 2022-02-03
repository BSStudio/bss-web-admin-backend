package hu.bsstudio.bssweb.videocrew.service

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DefaultVideoCrewServiceTest {

    @MockK
    private lateinit var mockRepository: VideoCrewRepository
    @MockK
    private lateinit var mockMapper: VideoCrewMapper

    private lateinit var underTest: DefaultVideoCrewService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultVideoCrewService(mockRepository, mockMapper)
    }

    @Test
    internal fun `should add position to video`() {
        every { mockMapper.modelToEntity(videoCrew1) } returns videoCrewEntity1
        every { mockMapper.entityToModel(videoCrewEntity1) } returns simpleVideoCrew1
        every { mockMapper.entityToModel(videoCrewEntity2) } returns simpleVideoCrew2
        every { mockRepository.save(videoCrewEntity1) } returns videoCrewEntity1
        every { mockRepository.findAllByVideoId(VIDEO_ID) } returns listOf(videoCrewEntity1, videoCrewEntity2)

        val result = underTest.addPosition(videoCrew1)

        assertThat(result).isEqualTo(listOf(simpleVideoCrew1, simpleVideoCrew2))
    }

    @Test
    internal fun `should remove position from video`() {
        every { mockMapper.modelToEntity(videoCrew1) } returns videoCrewEntity1
        every { mockMapper.entityToModel(videoCrewEntity2) } returns simpleVideoCrew2
        every { mockRepository.deleteById(videoCrewEntity1) } returns Unit
        every { mockRepository.findAllByVideoId(VIDEO_ID) } returns listOf(videoCrewEntity2)

        val result = underTest.removePosition(videoCrew1)

        assertThat(result).isEqualTo(listOf(simpleVideoCrew2))
    }

    private companion object {
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val POSITION_1 = "position1"
        private const val POSITION_2 = "position2"
        private val MEMBER_ID_1 = UUID.fromString("11234567-0123-0123-0123-0123456789ab")
        private val MEMBER_ID_2 = UUID.fromString("21234567-0123-0123-0123-0123456789ab")
        private val videoCrew1 = VideoCrew(VIDEO_ID, POSITION_1, MEMBER_ID_1)
        private val simpleVideoCrew1 = SimpleCrew(POSITION_1, MEMBER_ID_1)
        private val simpleVideoCrew2 = SimpleCrew(POSITION_2, MEMBER_ID_2)
        private val videoCrewEntity1 = VideoCrewEntity(VIDEO_ID, POSITION_1, MEMBER_ID_1)
        private val videoCrewEntity2 = VideoCrewEntity(VIDEO_ID, POSITION_2, MEMBER_ID_2)
    }
}
