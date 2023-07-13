package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.FileUpdate
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class FileUpdatingMemberServiceTest(
    @MockK private val mockService: MemberService,
    @MockK private val mockClient: FileApiClient
) {

    @InjectMockKs
    private lateinit var underTest: FileUpdatingMemberService

    @Test
    internal fun `should return all members`() {
        every { mockService.findAllMembers() } returns MEMBER_LIST

        val response = underTest.findAllMembers()

        Assertions.assertThat(response).isEqualTo(MEMBER_LIST)
    }

    @Test
    internal fun `should insert new member`() {
        every { mockService.insertMember(CREATE_MEMBER) } returns MEMBER
        every { MEMBER.id } returns MEMBER_ID
        every { MEMBER.url } returns URL
        every { mockClient.createMemberFolder(FILE_UPDATE) } returns FILE_UPDATE

        val response = underTest.insertMember(CREATE_MEMBER)

        Assertions.assertThat(response).isEqualTo(MEMBER)
    }

    @Test
    internal fun `should archive member`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockService.archiveMembers(memberIds) } returns memberIds

        val response = underTest.archiveMembers(memberIds)

        Assertions.assertThat(response).isEqualTo(memberIds)
    }

    @Test
    internal fun `should archive member with implicit archive flag`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockService.archiveMembers(memberIds, true) } returns memberIds

        val response = underTest.archiveMembers(memberIds, true)

        Assertions.assertThat(response).isEqualTo(memberIds)
    }

    @Test
    internal fun `should find member by id`() {
        every { mockService.findMemberById(MEMBER_ID) } returns Optional.of(MEMBER)

        val response = underTest.findMemberById(MEMBER_ID)

        Assertions.assertThat(response).hasValue(MEMBER)
    }

    @Test
    internal fun `should return empty if member was not found`() {
        every { mockService.findMemberById(MEMBER_ID) } returns Optional.empty()

        val response = underTest.findMemberById(MEMBER_ID)

        Assertions.assertThat(response).isEmpty
    }

    @Test
    internal fun `should not update member if id was not found`() {
        every { mockService.updateMember(MEMBER_ID, UPDATE_MEMBER) } returns Optional.empty()

        val response = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        Assertions.assertThat(response).isEmpty
    }

    @Test
    internal fun `should update member`() {
        every { mockService.updateMember(MEMBER_ID, UPDATE_MEMBER) } returns Optional.of(MEMBER)
        every { MEMBER.id } returns MEMBER_ID
        every { MEMBER.url } returns URL
        every { mockClient.updateMemberFolder(FILE_UPDATE) } returns FILE_UPDATE

        val response = underTest.updateMember(MEMBER_ID, UPDATE_MEMBER)

        Assertions.assertThat(response).hasValue(MEMBER)
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockService.removeMember(MEMBER_ID) } returns Unit

        underTest.removeMember(MEMBER_ID)
    }

    private companion object {
        private val MEMBER_ID = mockk<UUID>()
        private const val URL = "url"
        private val MEMBER = mockk<Member>()
        private val MEMBER_LIST = mockk<List<Member>>()
        private val CREATE_MEMBER = mockk<CreateMember>()
        private val UPDATE_MEMBER = mockk<UpdateMember>()
        private val FILE_UPDATE = FileUpdate(MEMBER_ID, URL)
    }
}
