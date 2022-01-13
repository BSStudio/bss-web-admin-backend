package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class MemberMapperTest {

    private lateinit var underTest: MemberMapper

    @BeforeEach
    internal fun setUp() {
        underTest = MemberMapper()
    }

    @Test
    internal fun `should map entity to model`() {
        val result = underTest.entityToModel(entity)

        assertThat(result).isEqualTo(model)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = underTest.modelToEntity(createMember)

        assertThat(result).isEqualTo(createdEntity)
    }

    private companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val IMAGE_URL = "imageUrl"
        private val JOINED_AT = LocalDate.of(2022, 1, 1)
        private const val ROLE = "role"
        private val STATUS = MemberStatus.MEMBER
        private const val ARCHIVED = false
        private val entity = MemberEntity(ID, NAME, DESCRIPTION, IMAGE_URL, JOINED_AT, ROLE, STATUS, ARCHIVED)
        private val model = Member(ID, NAME, DESCRIPTION, IMAGE_URL, JOINED_AT, ROLE, STATUS, ARCHIVED)
        private val createMember = CreateMember(ID, NAME)
        private val createdEntity = MemberEntity(ID, NAME)
    }
}
