package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.revision.mapper.RevisionMapper
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.history.Revision
import org.springframework.data.history.RevisionMetadata
import org.springframework.data.history.Revisions
import java.time.Instant
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultMemberServiceTest(
    @param:MockK private val mockRepository: MemberRepository,
    @param:MockK private val mockMapper: MemberMapper,
    @param:MockK private val mockRevisionMapper: RevisionMapper,
) {
    @InjectMockKs
    private lateinit var underTest: DefaultMemberService

    @Test
    internal fun `should return all members`() {
        every { mockRepository.findAll() } returns listOf(MEMBER_ENTITY)
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val actual = underTest.findAllMembers()

        actual.shouldContainExactly(MEMBER)
    }

    @Test
    internal fun `should insert new member`() {
        every { mockMapper.modelToEntity(CREATE_MEMBER) } returns MEMBER_ENTITY
        every { mockRepository.save(MEMBER_ENTITY) } returns MEMBER_ENTITY
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val actual = underTest.insertMember(CREATE_MEMBER)

        actual shouldBeEqual MEMBER
    }

    @Test
    internal fun `should archive member`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockRepository.findAllById(memberIds) } returns listOf(MEMBER_ENTITY)
        every { MEMBER_ENTITY.archived = true } returns Unit
        every { mockRepository.save(MEMBER_ENTITY) } returns MEMBER_ENTITY
        every { MEMBER_ENTITY.id } returns MEMBER_ID

        val actual = underTest.archiveMembers(memberIds, true)

        actual.shouldContainExactly(MEMBER_ID)
    }

    @Test
    internal fun `should change the status of members`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockRepository.findAllById(memberIds) } returns listOf(MEMBER_ENTITY)
        every { MEMBER_ENTITY.status = MemberStatus.ALUMNI } returns Unit
        every { mockRepository.save(MEMBER_ENTITY) } returns MEMBER_ENTITY
        every { MEMBER_ENTITY.id } returns MEMBER_ID

        val actual = underTest.changeMemberStatus(memberIds, MemberStatus.ALUMNI)

        actual.shouldContainExactly(MEMBER_ID)
        verify { MEMBER_ENTITY.status = MemberStatus.ALUMNI }
    }

    @Test
    internal fun `should find member by id`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.of(MEMBER_ENTITY)
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val actual = underTest.findMemberById(MEMBER_ID)

        actual shouldBePresent { it shouldBeEqual MEMBER }
    }

    @Test
    internal fun `should return empty if member was not found`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.empty()

        val actual = underTest.findMemberById(MEMBER_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should not update member if id was not found`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.empty()

        val actual = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should update member`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.of(MEMBER_ENTITY)
        every { mockMapper.updateToEntity(MEMBER_ENTITY, UPDATE_MEMBER) } returns MEMBER_ENTITY
        every { mockRepository.save(MEMBER_ENTITY) } returns MEMBER_ENTITY
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val actual = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        actual shouldBePresent { it shouldBeEqual MEMBER }
    }

    @Test
    internal fun `should mark the entity deleted`() {
        val entity = MemberEntity(url = "url", name = "name").apply { id = UUID.randomUUID() }
        every { mockRepository.findById(MEMBER_ID) } returns Optional.of(entity)
        every { mockRepository.save(entity) } returns entity

        underTest.removeMember(MEMBER_ID)

        entity.deletedAt.shouldNotBeNull()
    }

    @Test
    internal fun `should do nothing when deleting a missing entity`() {
        every { mockRepository.findById(MEMBER_ID) } returns Optional.empty()

        underTest.removeMember(MEMBER_ID)
    }

    @Test
    internal fun `should return deleted members`() {
        every { mockRepository.findAllDeleted(PAGEABLE) } returns PageImpl(listOf(MEMBER_ENTITY))
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val actual = underTest.findDeletedMembers(PAGEABLE)

        actual shouldBeEqual PageImpl(listOf(MEMBER))
    }

    @Test
    internal fun `should restore a deleted member`() {
        val entity =
            MemberEntity(url = "url", name = "name").apply {
                id = UUID.randomUUID()
                deletedAt = Instant.now()
            }
        every { mockRepository.findDeletedById(MEMBER_ID) } returns Optional.of(entity)
        every { mockRepository.save(entity) } returns entity
        every { mockMapper.entityToModel(entity) } returns MEMBER

        val actual = underTest.restoreMember(MEMBER_ID)

        actual shouldBePresent { it shouldBeEqual MEMBER }
        entity.deletedAt.shouldBeNull()
    }

    @Test
    internal fun `should not restore a member that is not deleted`() {
        every { mockRepository.findDeletedById(MEMBER_ID) } returns Optional.empty()

        val actual = underTest.restoreMember(MEMBER_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should return the revisions of a member`() {
        val revisions = Revisions.none<Long, MemberEntity>()
        every { mockRepository.findRevisions(MEMBER_ID) } returns revisions
        every { mockRevisionMapper.revisionsToModel(revisions, any<(MemberEntity) -> Instant?>()) } returns REVISION_INFOS

        val actual = underTest.findMemberRevisions(MEMBER_ID)

        actual shouldBeEqual REVISION_INFOS
    }

    @Test
    internal fun `should return a member as it was at a revision`() {
        every { mockRepository.findRevision(MEMBER_ID, REVISION) } returns Optional.of(Revision.of(REVISION_METADATA, MEMBER_ENTITY))
        every { mockMapper.entityToModel(MEMBER_ENTITY) } returns MEMBER

        val actual = underTest.findMemberRevision(MEMBER_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual MEMBER }
    }

    @Test
    internal fun `should give a member the fields it had at a revision`() {
        val current = MemberEntity(url = "url", name = "name").apply { id = UUID.randomUUID() }
        every { mockRepository.findRevision(MEMBER_ID, REVISION) } returns Optional.of(Revision.of(REVISION_METADATA, MEMBER_ENTITY))
        every { mockMapper.entityToUpdate(MEMBER_ENTITY) } returns UPDATE_MEMBER
        every { mockMapper.updateToEntity(current, UPDATE_MEMBER) } returns current
        every { mockRepository.findById(MEMBER_ID) } returns Optional.of(current)
        every { mockRepository.save(current) } returns current
        every { mockMapper.entityToModel(current) } returns MEMBER

        val actual = underTest.restoreMemberRevision(MEMBER_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual MEMBER }
    }

    @Test
    internal fun `should not restore a revision that is missing`() {
        every { mockRepository.findRevision(MEMBER_ID, REVISION) } returns Optional.empty()

        val actual = underTest.restoreMemberRevision(MEMBER_ID, REVISION)

        actual.shouldBeEmpty()
    }

    private companion object {
        private const val REVISION = 3L
        private val REVISION_METADATA = mockk<RevisionMetadata<Long>>()
        private val REVISION_INFOS = listOf(mockk<RevisionInfo>())
        private val PAGEABLE = Pageable.unpaged()
        private val MEMBER_ID = mockk<UUID>()
        private val MEMBER_ENTITY = mockk<MemberEntity>()
        private val MEMBER = mockk<Member>()
        private val CREATE_MEMBER = mockk<CreateMember>()
        private val UPDATE_MEMBER = mockk<UpdateMember>()
    }
}
