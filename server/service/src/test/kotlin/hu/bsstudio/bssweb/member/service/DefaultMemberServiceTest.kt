package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultMemberServiceTest(
    @MockK private val mockRepository: MemberRepository,
    @MockK private val mockMapper: MemberMapper
) {

    @InjectMockKs
    private lateinit var underTest: DefaultMemberService

    @Test
    internal fun `should return all members`() {
        every { mockRepository.findAll() } returns listOf(MEMBER_ENTITY)
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val response = underTest.findAllMembers()

        assertThat(response).containsExactly(MEMBER)
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
        every { MEMBER_ENTITY.archived = true } returns Unit
        every { mockRepository.save(MEMBER_ENTITY) } returns MEMBER_ENTITY
        every { MEMBER_ENTITY.id } returns MEMBER_ID

        val response = underTest.archiveMembers(memberIds)

        assertThat(response).isEqualTo(listOf(MEMBER_ID))
    }

    @Test
    internal fun `should archive member with implicit archive flag`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockRepository.findAllById(memberIds) } returns listOf(MEMBER_ENTITY)
        every { MEMBER_ENTITY.archived = true } returns Unit
        every { mockRepository.save(MEMBER_ENTITY) } returns MEMBER_ENTITY
        every { MEMBER_ENTITY.id } returns MEMBER_ID

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
        every { mockMapper.updateToEntity(MEMBER_ENTITY, UPDATE_MEMBER) } returns MEMBER_ENTITY
        every { mockRepository.save(MEMBER_ENTITY) } returns MEMBER_ENTITY
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val response = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        assertThat(response).hasValue(MEMBER)
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(MEMBER_ID) } returns Unit

        underTest.removeMember(MEMBER_ID)
    }

    private companion object {
        private val MEMBER_ID = mockk<UUID>()
        private val MEMBER_ENTITY = mockk<DetailedMemberEntity>()
        private val MEMBER = mockk<Member>()
        private val CREATE_MEMBER = mockk<CreateMember>()
        private val UPDATE_MEMBER = mockk<UpdateMember>()
    }
}
