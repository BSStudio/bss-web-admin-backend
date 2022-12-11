package hu.bsstudio.bssweb.videocrew.mapper

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class VideoCrewMapperTest {

    @InjectMockKs
    private lateinit var underTest: VideoCrewMapper

    @Test
    internal fun `should map model to entity id`() {
        val result = underTest.modelToId(VIDEO_CREW)

        assertThat(result).isEqualTo(VIDEO_CREW_ENTITY_ID)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = underTest.modelToEntity(VIDEO_CREW)

        assertThat(result).isEqualTo(VIDEO_CREW_ENTITY)
    }

    @Test
    internal fun `should map entity to model`() {
        val result = underTest.entityToModel(VIDEO_CREW_ENTITY)

        assertThat(result).isEqualTo(SIMPLE_CREW)
    }

    private companion object {
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val POSITION = "position"
        private val MEMBER_ID = UUID.fromString("11234567-0123-0123-0123-0123456789ab")
        private val VIDEO_CREW = VideoCrew(VIDEO_ID, POSITION, MEMBER_ID)
        private val VIDEO_CREW_ENTITY_ID = VideoCrewEntityId(VIDEO_ID, POSITION, MEMBER_ID)
        private val VIDEO_CREW_ENTITY = VideoCrewEntity(id = VIDEO_CREW_ENTITY_ID)
        private val SIMPLE_CREW = SimpleCrew(POSITION, MEMBER_ID)
    }
}
