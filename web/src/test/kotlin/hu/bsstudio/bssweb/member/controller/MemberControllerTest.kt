package hu.bsstudio.bssweb.member.controller

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.service.MemberService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.Optional

internal class MemberControllerTest {

    @MockK
    private lateinit var mockService: MemberService
    private lateinit var underTest: MemberController

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = MemberController(mockService)
    }

    @Test
    internal fun `should retrieve all members`() {
        every { mockService.findAllMembers() } returns MEMBER_LIST

        val response = underTest.getAllMembers()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER_LIST)
    }

    @Test
    internal fun `should retrieve a single member`() {
        every { mockService.findMemberById(ID) } returns Optional.of(MEMBER_1)

        val response = underTest.getMemberById(ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER_1)
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
        every { mockService.updateMember(ID, UPDATE_MEMBER) } returns Optional.of(MEMBER_1)

        val response = underTest.updateMember(ID, UPDATE_MEMBER)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER_1)
    }

    @Test
    internal fun `should return created on create`() {
        every { mockService.insertMember(CREATE_MEMBER) } returns MEMBER_1

        val response = underTest.createMember(CREATE_MEMBER)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(MEMBER_1)
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
        private const val ID = "id1"
        private const val NAME = "name1"
        private val CREATE_MEMBER = CreateMember(ID, NAME)
        private val UPDATE_MEMBER = UpdateMember(
            name = NAME,
            description = "description",
            imageUrl = "imageUrl",
            role = "role",
            status = MemberStatus.MEMBER,
            archived = false,
            joinedAt = LocalDate.EPOCH
        )
        private val MEMBER_1 = Member(
            id = ID,
            name = NAME,
            description = "description",
            imageUrl = "imageUrl",
            role = "role",
            status = MemberStatus.MEMBER,
            archived = false,
            joinedAt = LocalDate.EPOCH
        )
        private val MEMBER_2 = Member(
            id = "id2",
            name = "name2",
            description = "description",
            imageUrl = "imageUrl",
            role = "role",
            status = MemberStatus.MEMBER,
            archived = false,
            joinedAt = LocalDate.EPOCH
        )
        private val MEMBER_LIST = listOf(MEMBER_1, MEMBER_2)
    }
}
