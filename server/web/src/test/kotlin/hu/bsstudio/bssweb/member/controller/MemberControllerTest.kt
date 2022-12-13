package hu.bsstudio.bssweb.member.controller

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.service.MemberService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class MemberControllerTest {

    @MockK
    private lateinit var mockService: MemberService
    @InjectMockKs
    private lateinit var underTest: MemberController

    @Test
    internal fun `should retrieve all members`() {
        every { mockService.findAllMembers() } returns MEMBER_LIST

        val response = underTest.getAllMembers()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER_LIST)
    }

    @Test
    internal fun `should retrieve a single member`() {
        every { mockService.findMemberById(ID) } returns Optional.of(MEMBER)

        val response = underTest.getMemberById(ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER)
    }

    @Test
    internal fun `should retrieve a not found if member was not found`() {
        every { mockService.findMemberById(ID) } returns Optional.empty()

        val response = underTest.getMemberById(ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo(null)
    }

    @Test
    internal fun `should return ok on update`() {
        every { mockService.updateMember(ID, UPDATE_MEMBER) } returns Optional.of(MEMBER)

        val response = underTest.updateMember(ID, UPDATE_MEMBER)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER)
    }

    @Test
    internal fun `should return created on create`() {
        every { mockService.insertMember(CREATE_MEMBER) } returns MEMBER

        val response = underTest.createMember(CREATE_MEMBER)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(MEMBER)
    }

    @Test
    internal fun `should return ok and archived Ids`() {
        val memberIds = listOf(ID)
        val unArchive = true
        every { mockService.archiveMembers(memberIds, unArchive) } returns memberIds

        val response = underTest.archiveMembers(memberIds, unArchive)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(memberIds)
    }

    @Test
    internal fun `should return ok and archived Ids with default value`() {
        val memberIds = listOf(ID)
        every { mockService.archiveMembers(memberIds, true) } returns memberIds

        val response = underTest.archiveMembers(memberIds)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(memberIds)
    }

    @Test
    internal fun `should return ok after member was removed`() {
        every { mockService.removeMember(ID) } returns Unit

        underTest.removeMember(ID)
    }

    companion object {
        private val ID = mockk<UUID>()
        private val CREATE_MEMBER = mockk<CreateMember>()
        private val UPDATE_MEMBER = mockk<UpdateMember>()
        private val MEMBER = mockk<Member>()
        private val MEMBER_LIST = mockk<List<Member>>()
    }
}
