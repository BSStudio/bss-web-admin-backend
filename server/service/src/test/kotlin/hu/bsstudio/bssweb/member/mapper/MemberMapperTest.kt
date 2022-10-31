package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
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
        val result = underTest.entityToModel(ENTITY)

        assertThat(result).isEqualTo(MODEL)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = underTest.modelToEntity(CREATE_MEMBER)

        assertThat(result).isEqualTo(CREATED_ENTITY)
    }

    private companion object {
        private val ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val URL = "url"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private val JOINED_AT = LocalDate.of(2022, 1, 1)
        private const val ROLE = "role"
        private val STATUS = MemberStatus.MEMBER
        private const val ARCHIVED = false
        private val ENTITY = MemberEntity(ID, URL, NAME, DESCRIPTION, JOINED_AT, ROLE, STATUS, ARCHIVED)
        private val MODEL = Member(ID, URL, NAME, DESCRIPTION, JOINED_AT, ROLE, STATUS, ARCHIVED)
        private val CREATE_MEMBER = CreateMember(URL, NAME)
        private val CREATED_ENTITY = MemberEntity(id = ID, url = URL, name = NAME)
    }
}
