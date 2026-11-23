package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.MemberFileUpdate
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import io.kotest.matchers.equals.shouldBeEqual
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
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class FileUpdatingMemberServiceTest(
    @param:MockK private val mockService: MemberService,
    @param:MockK private val mockClient: FileApiClient,
) {
    @InjectMockKs
    private lateinit var underTest: FileUpdatingMemberService

    @Test
    internal fun `should return all members`() {
        every { mockService.findAllMembers() } returns MEMBER_LIST

        val actual = underTest.findAllMembers()

        actual shouldBeEqual MEMBER_LIST
    }

    @Test
    internal fun `should insert new member`() {
        every { mockService.insertMember(CREATE_MEMBER) } returns MEMBER
        every { MEMBER.id } returns MEMBER_ID
        every { MEMBER.url } returns URL
        every { mockClient.createMemberFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.insertMember(CREATE_MEMBER)

        actual shouldBeEqual MEMBER
    }

    @Test
    internal fun `should archive member`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockService.archiveMembers(memberIds, true) } returns memberIds

        val actual = underTest.archiveMembers(memberIds, true)

        actual shouldBeEqual memberIds
    }

    @Test
    internal fun `should unarchive member`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockService.archiveMembers(memberIds, false) } returns memberIds

        val actual = underTest.archiveMembers(memberIds, false)

        actual shouldBeEqual memberIds
    }

    @Test
    internal fun `should change the status of members`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockService.changeMemberStatus(memberIds, MemberStatus.ALUMNI) } returns memberIds

        val actual = underTest.changeMemberStatus(memberIds, MemberStatus.ALUMNI)

        actual shouldBeEqual memberIds
    }

    @Test
    internal fun `should find member by id`() {
        every { mockService.findMemberById(MEMBER_ID) } returns Optional.of(MEMBER)

        val actual = underTest.findMemberById(MEMBER_ID)

        actual shouldBePresent { it shouldBeEqual MEMBER }
    }

    @Test
    internal fun `should return empty if member was not found`() {
        every { mockService.findMemberById(MEMBER_ID) } returns Optional.empty()

        val actual = underTest.findMemberById(MEMBER_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should not update member if id was not found`() {
        every { mockService.updateMember(MEMBER_ID, UPDATE_MEMBER) } returns Optional.empty()

        val actual = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should update member`() {
        every { mockService.updateMember(MEMBER_ID, UPDATE_MEMBER) } returns Optional.of(MEMBER)
        every { MEMBER.id } returns MEMBER_ID
        every { MEMBER.url } returns URL
        every { mockClient.updateMemberFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        actual shouldBePresent { it shouldBeEqual MEMBER }
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockService.removeMember(MEMBER_ID) } returns Unit

        underTest.removeMember(MEMBER_ID)
    }

    @Test
    internal fun `should return deleted members`() {
        every { mockService.findDeletedMembers(PAGEABLE) } returns PAGED_MEMBERS

        val actual = underTest.findDeletedMembers(PAGEABLE)

        actual shouldBeEqual PAGED_MEMBERS
    }

    @Test
    internal fun `should restore a member and update its folder`() {
        every { mockService.restoreMember(MEMBER_ID) } returns Optional.of(MEMBER)
        every { MEMBER.id } returns MEMBER_ID
        every { MEMBER.url } returns URL
        every { mockClient.updateMemberFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.restoreMember(MEMBER_ID)

        actual shouldBePresent { it shouldBeEqual MEMBER }
        verify { mockClient.updateMemberFolder(FILE_UPDATE) }
    }

    @Test
    internal fun `should return the revisions of a member`() {
        every { mockService.findMemberRevisions(MEMBER_ID) } returns REVISION_INFOS

        val actual = underTest.findMemberRevisions(MEMBER_ID)

        actual shouldBeEqual REVISION_INFOS
    }

    @Test
    internal fun `should return a member as it was at a revision`() {
        every { mockService.findMemberRevision(MEMBER_ID, REVISION) } returns Optional.of(MEMBER)

        val actual = underTest.findMemberRevision(MEMBER_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual MEMBER }
    }

    @Test
    internal fun `should restore a revision of a member and update its folder`() {
        every { mockService.restoreMemberRevision(MEMBER_ID, REVISION) } returns Optional.of(MEMBER)
        every { MEMBER.id } returns MEMBER_ID
        every { MEMBER.url } returns URL
        every { mockClient.updateMemberFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.restoreMemberRevision(MEMBER_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual MEMBER }
        verify { mockClient.updateMemberFolder(FILE_UPDATE) }
    }

    private companion object {
        private const val REVISION = 3L
        private val REVISION_INFOS = listOf(mockk<RevisionInfo>())
        private val PAGEABLE = mockk<Pageable>()
        private val PAGED_MEMBERS = Page.empty<Member>()
        private val MEMBER_ID = mockk<UUID>()
        private const val URL = "url"
        private val MEMBER = mockk<Member>()
        private val MEMBER_LIST = listOf<Member>()
        private val CREATE_MEMBER = mockk<CreateMember>()
        private val UPDATE_MEMBER = mockk<UpdateMember>()
        private val FILE_UPDATE = MemberFileUpdate(MEMBER_ID, URL)
    }
}
