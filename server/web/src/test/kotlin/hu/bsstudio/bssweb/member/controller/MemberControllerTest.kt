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
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(MemberController::class)
@ContextConfiguration(classes = [MemberControllerTest::class])
internal class MemberControllerTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var mockService: MemberService

    @Test
    internal fun `should retrieve all members`() {
        every { mockService.findAllMembers() } returns MEMBER_LIST

        mockMvc.get("/api/v1/member").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(MEMBER_LIST) }
        }
    }

    @Test
    internal fun `should retrieve a single member`() {
        every { mockService.findMemberById(ID) } returns Optional.of(MEMBER)

        mockMvc.get("/api/v1/member/$ID").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(MEMBER) }
        }
    }

    @Test
    internal fun `should retrieve a not found if member was not found`() {
        every { mockService.findMemberById(ID) } returns Optional.empty()

        mockMvc.get("/api/v1/member/$ID").andExpectAll {
            status { isNotFound() }
            content { string("") }
        }
    }

    @Test
    internal fun `should return ok on update`() {
        every { mockService.updateMember(ID, UPDATE_MEMBER) } returns Optional.of(MEMBER)

        mockMvc.put("/api/v1/member/$ID") {
            content = objectMapper.writeValueAsString(UPDATE_MEMBER)
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(MEMBER) }
        }
    }

    @Test
    internal fun `should return created on create`() {
        every { mockService.insertMember(CREATE_MEMBER) } returns MEMBER

        mockMvc.post("/api/v1/member") {
            content = objectMapper.writeValueAsString(CREATE_MEMBER)
        }.andExpectAll {
            status { isCreated() }
            content { objectMapper.writeValueAsString(MEMBER) }
        }
    }

    @Test
    internal fun `should return ok and archived Ids`() {
        val memberIds = listOf(ID)
        val unArchive = true
        every { mockService.archiveMembers(memberIds, unArchive) } returns memberIds

        mockMvc.put("/api/v1/member/archive") {
            content = objectMapper.writeValueAsString(memberIds)
            param("unArchive", unArchive.toString())
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(memberIds) }
        }
    }

    @Test
    internal fun `should return ok and archived Ids with default value`() {
        val memberIds = listOf(ID)
        every { mockService.archiveMembers(memberIds, true) } returns memberIds

        mockMvc.put("/api/v1/member/archive") {
            content = objectMapper.writeValueAsString(memberIds)
        }.andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(memberIds) }
        }
    }

    @Test
    internal fun `should return ok after member was removed`() {
        every { mockService.removeMember(ID) } returns Unit

        mockMvc.delete("/api/v1/member/$ID").andExpectAll {
            status { isOk() }
            content { string("") }
        }
    }

    private companion object {
        private val ID = UUID.randomUUID()
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
        private val MEMBER = Member(url = URL, name = NAME, id = ID, nickname = NICKNAME, description = DESCRIPTION, joinedAt = JOINED_AT, role = ROLE, status = STATUS, archived = ARCHIVED)
        private val MEMBER_LIST = listOf(MEMBER)
    }
}
