package hu.bsstudio.bssweb.controller

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
import java.util.Optional
import java.util.UUID

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
        every { mockService.getAllMembers() } returns MEMBER_LIST

        val response = underTest.getAllMembers()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER_LIST)
    }

    @Test
    internal fun `should retrieve a single member`() {
        every { mockService.getMemberById(UUID_1) } returns Optional.of(MEMBER_1)

        val response = underTest.getMemberById(UUID_STRING)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER_1)
    }

    @Test
    internal fun `should retrieve a not found if member was not found`() {
        every { mockService.getMemberById(UUID_1) } returns Optional.empty()

        val response = underTest.getMemberById(UUID_STRING)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo(null)
    }

    @Test
    internal fun `should return ok on update`() {
        val updateMember = UpdateMember("name")
        every { mockService.updateMember(updateMember) } returns MEMBER_1

        val response = underTest.updateMember(updateMember)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(MEMBER_1)
    }

    @Test
    internal fun `should return created on create`() {
        val createMember = CreateMember("name")
        every { mockService.createMember(createMember) } returns MEMBER_1

        val response = underTest.createMember(createMember)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(MEMBER_1)
    }

    companion object {
        private const val UUID_STRING = "01234567-0123-0123-0123-0123456789ab"
        private val UUID_1 = UUID.fromString(UUID_STRING)
        private val MEMBER_1 = Member(
            id = UUID_1,
            name = "name",
            description = "description",
            imageUrl = "imageUrl",
            role = "role",
            status = MemberStatus.MEMBER
        )
        private val MEMBER_2 = Member(
            id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
            name = "name",
            description = "description",
            imageUrl = "imageUrl",
            role = "role",
            status = MemberStatus.MEMBER
        )
        private val MEMBER_LIST = listOf(MEMBER_1, MEMBER_2)
    }
}
