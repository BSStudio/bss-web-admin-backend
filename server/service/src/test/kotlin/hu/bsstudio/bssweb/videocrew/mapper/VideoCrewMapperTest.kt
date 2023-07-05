package hu.bsstudio.bssweb.videocrew.mapper

import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.SimpleMember
import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class VideoCrewMapperTest {

    @MockK
    private lateinit var memberMapper: MemberMapper

    @InjectMockKs
    private lateinit var underTest: VideoCrewMapper

    @Test
    internal fun `should map model to entity id`() {
        val result = underTest.modelToId(VIDEO_CREW_REQUEST)

        assertThat(result).isEqualTo(VIDEO_CREW_ENTITY_ID)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = underTest.modelToEntity(VIDEO_CREW_REQUEST)

        assertThat(result).isEqualTo(VIDEO_CREW_ENTITY)
    }

    @Test
    internal fun `should map entity to model`() {
        every { memberMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val result = underTest.entityToModel(DETAILED_VIDEO_CREW_ENTITY)

        assertThat(result).isEqualTo(VIDEO_CREW)
    }

    private companion object {
        private val VIDEO_ID = mockk<UUID>()
        private const val POSITION = "position"
        private val MEMBER_ID = UUID.fromString("11234567-0123-0123-0123-0123456789ab")
        private val MEMBER = SimpleMember(MEMBER_ID, "name", "nickname")
        private val VIDEO_CREW = VideoCrew(VIDEO_ID, POSITION, MEMBER)
        private val VIDEO_CREW_REQUEST = VideoCrewRequest(VIDEO_ID, POSITION, MEMBER_ID)
        private val VIDEO_CREW_ENTITY_ID = VideoCrewEntityId(VIDEO_ID, POSITION, MEMBER_ID)
        private val VIDEO_CREW_ENTITY = VideoCrewEntity(VIDEO_CREW_ENTITY_ID)
        private val MEMBER_ENTITY = SimpleMemberEntity(MEMBER_ID, "name", "nickname")
        private val DETAILED_VIDEO_CREW_ENTITY = DetailedVideoCrewEntity(id = VIDEO_CREW_ENTITY_ID, member = MEMBER_ENTITY)
    }
}
