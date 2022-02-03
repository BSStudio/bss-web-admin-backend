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
        val result = underTest.entityToModel(entity)

        assertThat(result).isEqualTo(model)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = underTest.modelToEntity(createMember)

        assertThat(result).isEqualTo(createdEntity)
    }

    private companion object {
        private val ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val URL = "url"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val IMAGE_URL = "imageUrl"
        private val JOINED_AT = LocalDate.of(2022, 1, 1)
        private const val ROLE = "role"
        private val STATUS = MemberStatus.MEMBER
        private const val ARCHIVED = false
        private val entity = MemberEntity(ID, URL, NAME, DESCRIPTION, IMAGE_URL, JOINED_AT, ROLE, STATUS, ARCHIVED)
        private val model = Member(ID, URL, NAME, DESCRIPTION, IMAGE_URL, JOINED_AT, ROLE, STATUS, ARCHIVED)
        private val createMember = CreateMember(URL, NAME)
        private val createdEntity = MemberEntity(id = ID, url = URL, name = NAME)
    }
}
