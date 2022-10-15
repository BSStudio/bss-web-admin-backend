package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultMemberServiceTest {

    @MockK
    private lateinit var mockRepository: MemberRepository
    @MockK
    private lateinit var mockMapper: MemberMapper
    @InjectMockKs
    private lateinit var underTest: DefaultMemberService

    @Test
    internal fun `should return all members`() {
        every { mockRepository.findAll() } returns MEMBER_ENTITY_LIST
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val response = underTest.findAllMembers()

        assertThat(response).isEqualTo(MEMBER_LIST)
    }

    @Test
    internal fun `should insert new member`() {
        every { mockMapper.modelToEntity(CREATE_MEMBER) } returns MEMBER_ENTITY
        every { mockRepository.save(MEMBER_ENTITY) } returns MEMBER_ENTITY
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val response = underTest.insertMember(CREATE_MEMBER)

        assertThat(response).isEqualTo(MEMBER)
    }

    @Test
    internal fun `should archive member`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockRepository.findAllById(memberIds) } returns listOf(MEMBER_ENTITY)
        val updatedMemberEntity = MEMBER_ENTITY.copy(archived = true)
        every { mockRepository.save(updatedMemberEntity) } returns updatedMemberEntity
        every { mockMapper.entityToModel(updatedMemberEntity) } returns MEMBER

        val response = underTest.archiveMembers(memberIds)

        assertThat(response).isEqualTo(listOf(MEMBER_ID))
    }

    @Test
    internal fun `should archive member with implicit archive flag`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockRepository.findAllById(memberIds) } returns listOf(MEMBER_ENTITY)
        val updatedMemberEntity = MEMBER_ENTITY.copy(archived = true)
        every { mockRepository.save(updatedMemberEntity) } returns updatedMemberEntity
        every { mockMapper.entityToModel(updatedMemberEntity) } returns MEMBER

        val response = underTest.archiveMembers(memberIds, true)

        assertThat(response).isEqualTo(listOf(MEMBER_ID))
    }

    @Test
    internal fun `should find member by id`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.of(MEMBER_ENTITY)
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val response = underTest.findMemberById(MEMBER_ID)

        assertThat(response).hasValue(MEMBER)
    }

    @Test
    internal fun `should return empty if member was not found`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.empty()

        val response = underTest.findMemberById(MEMBER_ID)

        assertThat(response).isEmpty
    }

    @Test
    internal fun `should not update member if id was not found`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.empty()

        val response = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        assertThat(response).isEmpty
    }

    @Test
    internal fun `should update member`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.of(MEMBER_ENTITY)
        val updatedEntity = MEMBER_ENTITY.copy(
            url = UPDATE_MEMBER.url,
            name = UPDATE_MEMBER.name,
            description = UPDATE_MEMBER.description,
            imageUrl = UPDATE_MEMBER.imageUrl,
            joinedAt = UPDATE_MEMBER.joinedAt,
            role = UPDATE_MEMBER.role,
            status = UPDATE_MEMBER.status,
            archived = UPDATE_MEMBER.archived
        )
        every { mockRepository.save(updatedEntity) } returns updatedEntity
        val updatedMember = MEMBER.copy(
            url = UPDATE_MEMBER.url,
            name = UPDATE_MEMBER.name,
            description = UPDATE_MEMBER.description,
            imageUrl = UPDATE_MEMBER.imageUrl,
            joinedAt = UPDATE_MEMBER.joinedAt,
            role = UPDATE_MEMBER.role,
            status = UPDATE_MEMBER.status,
            archived = UPDATE_MEMBER.archived
        )
        every { mockMapper.entityToModel(updatedEntity) } returns updatedMember

        val response = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        assertThat(response).hasValue(updatedMember)
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(MEMBER_ID) } returns Unit

        underTest.removeMember(MEMBER_ID)
    }

    private companion object {
        private val MEMBER_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private val MEMBER_ENTITY = MemberEntity(MEMBER_ID, "url", "name", "description", "imageUrl", LocalDate.of(2022, 1, 1), "role", MemberStatus.MEMBER, false)
        private val MEMBER_ENTITY_LIST = listOf(MEMBER_ENTITY)
        private val MEMBER = Member(MEMBER_ID, "url", "name", "description", "imageUrl", LocalDate.of(2022, 1, 1), "role", MemberStatus.MEMBER, false)
        private val MEMBER_LIST = listOf(MEMBER)
        private val CREATE_MEMBER = CreateMember("url", "name")
        private val UPDATE_MEMBER = UpdateMember("updatedUrl", "updatedName", "updatedDescription", "imageUrl", LocalDate.of(2022, 2, 2), "updatedRole", MemberStatus.ALUMNI, true)
    }
}
