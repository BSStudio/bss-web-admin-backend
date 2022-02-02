package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

internal class DefaultMemberServiceTest {

    @MockK
    private lateinit var mockRepository: MemberRepository
    @MockK
    private lateinit var mockMapper: MemberMapper
    private lateinit var underTest: DefaultMemberService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultMemberService(mockRepository, mockMapper)
    }

    @Test
    internal fun `should return all members`() {
        every { mockRepository.findAll() } returns memberEntityList
        every { mockMapper.entityToModel(memberEntity1) } returns member1

        val response = underTest.findAllMembers()

        Assertions.assertThat(response).isEqualTo(memberList)
    }

    @Test
    internal fun `should insert new member`() {
        every { mockMapper.modelToEntity(createMember) } returns memberEntity1
        every { mockRepository.save(memberEntity1) } returns memberEntity1
        every { mockMapper.entityToModel(memberEntity1) } returns member1

        val response = underTest.insertMember(createMember)

        Assertions.assertThat(response).isEqualTo(member1)
    }

    @Test
    internal fun `should archive member`() {
        val memberIds = listOf(memberId1)
        every { mockRepository.findAllById(memberIds) } returns listOf(memberEntity1)
        val updatedMemberEntity = memberEntity1.copy(archived = true)
        every { mockRepository.save(updatedMemberEntity) } returns updatedMemberEntity
        every { mockMapper.entityToModel(updatedMemberEntity) } returns member1

        val response = underTest.archiveMembers(memberIds)

        Assertions.assertThat(response).isEqualTo(listOf(memberId1))
    }

    @Test
    internal fun `should archive member with implicit archive flag`() {
        val memberIds = listOf(memberId1)
        every { mockRepository.findAllById(memberIds) } returns listOf(memberEntity1)
        val updatedMemberEntity = memberEntity1.copy(archived = true)
        every { mockRepository.save(updatedMemberEntity) } returns updatedMemberEntity
        every { mockMapper.entityToModel(updatedMemberEntity) } returns member1

        val response = underTest.archiveMembers(memberIds, true)

        Assertions.assertThat(response).isEqualTo(listOf(memberId1))
    }

    @Test
    internal fun `should find member by id`() {
        every { mockRepository.findById(memberId1) } returns Optional.of(memberEntity1)
        every { mockMapper.entityToModel(memberEntity1) } returns member1

        val response = underTest.findMemberById(memberId1)

        Assertions.assertThat(response).isEqualTo(Optional.of(member1))
    }

    @Test
    internal fun `should return empty if member was not found`() {
        every { mockRepository.findById(memberId1) } returns Optional.empty()

        val response = underTest.findMemberById(memberId1)

        Assertions.assertThat(response).isEqualTo(Optional.empty<Member>())
    }

    @Test
    internal fun `should not update member if id was not found`() {
        every { mockRepository.findById(memberId1) } returns Optional.empty()

        val response = underTest.updateMember(memberId1, updateMember)

        Assertions.assertThat(response).isEqualTo(Optional.empty<Member>())
    }

    @Test
    internal fun `should update member`() {
        every { mockRepository.findById(memberId1) } returns Optional.of(memberEntity1)
        val updatedEntity = memberEntity1.copy(
            url = updateMember.url,
            name = updateMember.name,
            description = updateMember.description,
            imageUrl = updateMember.imageUrl,
            joinedAt = updateMember.joinedAt,
            role = updateMember.role,
            status = updateMember.status,
            archived = updateMember.archived
        )
        every { mockRepository.save(updatedEntity) } returns updatedEntity
        val updatedMember = member1.copy(
            url = updateMember.url,
            name = updateMember.name,
            description = updateMember.description,
            imageUrl = updateMember.imageUrl,
            joinedAt = updateMember.joinedAt,
            role = updateMember.role,
            status = updateMember.status,
            archived = updateMember.archived
        )
        every { mockMapper.entityToModel(updatedEntity) } returns updatedMember

        val response = underTest.updateMember(memberId1, updateMember)

        Assertions.assertThat(response).isEqualTo(Optional.of(updatedMember))
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(memberId1) } returns Unit

        underTest.removeMember(memberId1)
    }

    private companion object {
        private val memberId1 = UUID.fromString("01234567-0123-0123-0123-0123456789AB")
        private val memberEntity1 = MemberEntity(memberId1, "url", "name", "description", "imageUrl", LocalDate.of(2022, 1, 1), "role", MemberStatus.MEMBER, false)
        private val memberEntityList = listOf(memberEntity1)
        private val member1 = Member(memberId1, "url", "name", "description", "imageUrl", LocalDate.of(2022, 1, 1), "role", MemberStatus.MEMBER, false)
        private val memberList = listOf(member1)
        private val createMember = CreateMember("url", "name")
        private val updateMember = UpdateMember("updatedUrl", "updatedName", "updatedDescription", "imageUrl", LocalDate.of(2022, 2, 2), "updatedRole", MemberStatus.ALUMNI, true)
    }
}
