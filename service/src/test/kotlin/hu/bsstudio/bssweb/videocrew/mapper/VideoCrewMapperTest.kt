package hu.bsstudio.bssweb.videocrew.mapper

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class VideoCrewMapperTest {

    @InjectMockKs
    private lateinit var underTest: VideoCrewMapper

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
        private val videoId = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val position = "position"
        private val memberId = UUID.fromString("11234567-0123-0123-0123-0123456789ab")
        private val videoCrew = VideoCrew(videoId, position, memberId)
        private val videoCrewEntity = VideoCrewEntity(videoId, position, memberId)
        private val simpleCrew = SimpleCrew(position, memberId)
    }
}
