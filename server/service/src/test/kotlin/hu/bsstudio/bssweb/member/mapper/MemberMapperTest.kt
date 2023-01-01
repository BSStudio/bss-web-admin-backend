package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.SimpleMember
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

internal class MemberMapperTest {

    private lateinit var underTest: MemberMapper

    @BeforeEach
    internal fun setUp() {
        val idGenerator = { ID }
        underTest = MemberMapper()
        underTest = MemberMapper(idGenerator)
    }

    @Test
    internal fun `should map entity to model`() {
        val entity = MemberEntity(ID, URL, NAME, NICKNAME, DESCRIPTION, JOINED_AT, ROLE, STATUS, ARCHIVED)

        val result = underTest.entityToModel(entity)

        val model = Member(ID, URL, NAME, NICKNAME, DESCRIPTION, JOINED_AT, ROLE, STATUS, ARCHIVED)
        assertThat(result).isEqualTo(model)
    }

    @Test
    internal fun `should map simple entity to model`() {
        val entity = SimpleMemberEntity(ID, NAME, NICKNAME)

        val result = underTest.entityToModel(entity)

        val model = SimpleMember(ID, NAME, NICKNAME)
        assertThat(result).isEqualTo(model)
    }

    @Test
    internal fun `should map model to entity`() {
        val model = CreateMember(URL, NAME)

        val result = underTest.modelToEntity(model)

        val entity = MemberEntity(id = ID, url = URL, name = NAME)
        assertThat(result).isEqualTo(entity)
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
    }
}
