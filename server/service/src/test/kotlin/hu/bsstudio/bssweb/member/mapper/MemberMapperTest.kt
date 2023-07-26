package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.SimpleMember
import hu.bsstudio.bssweb.member.model.UpdateMember
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class MemberMapperTest {

    @InjectMockKs
    private lateinit var underTest: MemberMapper

    @Test
    internal fun `should map entity to model`() {
        val result = underTest.entityToModel(MEMBER_ENTITY)

        assertThat(result).isEqualTo(MEMBER)
    }

    @Test
    internal fun `should map simple entity to model`() {
        val result = underTest.entityToModel(SIMPLE_MEMBER_ENTITY)

        assertThat(result).isEqualTo(SIMPLE_MEMBER)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = underTest.modelToEntity(CREATE_MEMBER).apply { id = ID }

        assertThat(result).isEqualTo(CREATE_MEMBER_ENTITY)
    }

    @Test
    internal fun `should map update model to entity`() {
        val result = underTest.updateToEntity(MEMBER_ENTITY, MEMBER_UPDATE)

        assertThat(result).isEqualTo(UPDATED_MEMBER_ENTITY)
    }

    private companion object {
        private val ID = mockk<UUID>()
        private const val URL = "url"
        private const val NAME = "name"
        private const val NICKNAME = "nickname"
        private const val DESCRIPTION = "description"
        private val JOINED_AT = mockk<LocalDate>()
        private const val ROLE = "role"
        private val STATUS = mockk<MemberStatus>()
        private const val ARCHIVED = false
        private val MEMBER_ENTITY = MemberEntity(URL, NAME, NICKNAME, DESCRIPTION, JOINED_AT, ROLE, STATUS, ARCHIVED).apply { id = ID }
        private val MEMBER = Member(ID, URL, NAME, NICKNAME, DESCRIPTION, JOINED_AT, ROLE, STATUS, ARCHIVED)
        private val SIMPLE_MEMBER_ENTITY = SimpleMemberEntity(NAME, NICKNAME).apply { id = ID }
        private val SIMPLE_MEMBER = SimpleMember(ID, NAME, NICKNAME)
        private val CREATE_MEMBER = CreateMember(URL, NAME)
        private val CREATE_MEMBER_ENTITY = MemberEntity(url = URL, name = NAME).apply { id = ID }
        private const val NEW_URL = "NEW_URL"
        private const val NEW_NAME = "NEW_NAME"
        private const val NEW_NICKNAME = "NEW_NICKNAME"
        private const val NEW_DESCRIPTION = "NEW_DESCRIPTION"
        private val NEW_JOINED_AT = mockk<LocalDate>()
        private const val NEW_ROLE = "NEW_ROLE"
        private val NEW_STATUS = mockk<MemberStatus>()
        private const val NEW_ARCHIVED = true
        private val MEMBER_UPDATE = UpdateMember(NEW_URL, NEW_NAME, NEW_NICKNAME, NEW_DESCRIPTION, NEW_JOINED_AT, NEW_ROLE, NEW_STATUS, NEW_ARCHIVED)
        private val UPDATED_MEMBER_ENTITY = MemberEntity(NEW_URL, NEW_NAME, NEW_NICKNAME, NEW_DESCRIPTION, NEW_JOINED_AT, NEW_ROLE, NEW_STATUS, NEW_ARCHIVED).apply { id = ID }
    }
}
