package hu.bsstudio.bssweb.videocrew.mapper

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class VideoCrewMapperTest {

    private lateinit var underTest: VideoCrewMapper

    @BeforeEach
    internal fun setUp() {
        underTest = VideoCrewMapper()
    }

    @Test
    internal fun `should map entity to model`() {
        val result = underTest.modelToEntity(videoCrew)

        assertThat(result).isEqualTo(videoCrewEntity)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = underTest.entityToModel(videoCrewEntity)

        assertThat(result).isEqualTo(simpleCrew)
    }

    private companion object {
        private val videoId = UUID.fromString("01234567-0123-0123-0123-0123456789AB")
        private const val position = "position"
        private val memberId = UUID.fromString("11234567-0123-0123-0123-0123456789AB")
        private val videoCrew = VideoCrew(videoId, position, memberId)
        private val videoCrewEntity = VideoCrewEntity(videoId, position, memberId)
        private val simpleCrew = SimpleCrew(position, memberId)
    }
}
