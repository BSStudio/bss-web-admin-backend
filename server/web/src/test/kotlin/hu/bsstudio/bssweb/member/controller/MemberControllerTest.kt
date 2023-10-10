package hu.bsstudio.bssweb.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.service.MemberService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(MemberController::class)
internal class MemberControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {

    @MockkBean
    private lateinit var mockService: MemberService

    @Test
    internal fun `should retrieve all members`() {
        every { mockService.findAllMembers() } returns MEMBER_LIST

        mockMvc.get(BASE_URL) {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(MEMBER_LIST) }
        }
    }

    @Test
    internal fun `should retrieve a single member`() {
        every { mockService.findMemberById(MEMBER_ID) } returns Optional.of(MEMBER)

        mockMvc.get("$BASE_URL/$MEMBER_ID") {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(MEMBER) }
        }
    }

    @Test
    internal fun `should retrieve a not found if member was not found`() {
        every { mockService.findMemberById(MEMBER_ID) } returns Optional.empty()

        mockMvc.get("$BASE_URL/$MEMBER_ID") {
            with(httpBasic(USERNAME, PASSWORD))
        }.andExpectAll {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun `should return ok on update`() {
        every { mockService.updateMember(MEMBER_ID, UPDATE_MEMBER) } returns Optional.of(MEMBER)

        mockMvc.put("$BASE_URL/$MEMBER_ID") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(UPDATE_MEMBER)
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(MEMBER) }
        }
    }

    @Test
    internal fun `should return created on create`() {
        every { mockService.insertMember(CREATE_MEMBER) } returns MEMBER

        mockMvc.post(BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(CREATE_MEMBER)
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpectAll {
            status { isCreated() }
            content { objectMapper.writeValueAsString(MEMBER) }
            header { string("Location", "http://localhost$BASE_URL/$MEMBER_ID") }
        }
    }

    @Test
    internal fun `should return ok and archived Ids`() {
        val memberIds = listOf(MEMBER_ID)
        val unArchive = true
        every { mockService.archiveMembers(memberIds, unArchive) } returns memberIds

        mockMvc.put("$BASE_URL/archive") {
            param("memberIds", MEMBER_ID.toString())
            param("unArchive", "$unArchive")
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(memberIds) }
        }
    }

    @Test
    internal fun `should return ok and archived Ids with default value`() {
        val memberIds = listOf(MEMBER_ID)
        every { mockService.archiveMembers(memberIds, true) } returns memberIds

        mockMvc.put("$BASE_URL/archive") {
            param("memberIds", MEMBER_ID.toString())
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(memberIds) }
        }
    }

    @Test
    internal fun `should return ok after member was removed`() {
        every { mockService.removeMember(MEMBER_ID) } returns Unit

        mockMvc.delete("$BASE_URL/$MEMBER_ID") {
            with(httpBasic(USERNAME, PASSWORD))
            with(csrf())
        }.andExpectAll {
            status { isNoContent() }
            content { string("") }
        }
    }

    private companion object {
        private const val BASE_URL = "/api/v1/member"
        private const val USERNAME = "user"
        private const val PASSWORD = "password"
        private val MEMBER_ID = UUID.randomUUID()
        private const val URL = "url"
        private const val NAME = "name"
        private val CREATE_MEMBER = CreateMember(url = URL, name = NAME)
        private const val NICKNAME = "nickname"
        private const val DESCRIPTION = "description"
        private val JOINED_AT = LocalDate.now()
        private const val ROLE = "Director"
        private val STATUS = MemberStatus.ACTIVE_ALUMNI
        private const val ARCHIVED = false
        private val UPDATE_MEMBER = UpdateMember(url = URL, name = NAME, nickname = NICKNAME, description = DESCRIPTION, joinedAt = JOINED_AT, role = ROLE, status = STATUS, archived = ARCHIVED)
        private val MEMBER = Member(url = URL, name = NAME, id = MEMBER_ID, nickname = NICKNAME, description = DESCRIPTION, joinedAt = JOINED_AT, role = ROLE, status = STATUS, archived = ARCHIVED)
        private val MEMBER_LIST = listOf(MEMBER)
    }
}
