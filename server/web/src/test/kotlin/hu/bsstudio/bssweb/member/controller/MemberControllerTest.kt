package hu.bsstudio.bssweb.member.controller

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import hu.bsstudio.bssweb.revision.model.RevisionType
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import tools.jackson.databind.ObjectMapper
import java.time.Instant
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(MemberController::class)
internal class MemberControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val objectMapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var mockService: MemberService

    @Test
    internal fun `should retrieve all members`() {
        every { mockService.findAllMembers() } returns MEMBER_LIST

        mockMvc
            .get(BASE_URL) {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(MEMBER_LIST) }
            }
    }

    @Test
    internal fun `should retrieve a single member`() {
        every { mockService.findMemberById(MEMBER_ID) } returns Optional.of(MEMBER)

        mockMvc
            .get("$BASE_URL/$MEMBER_ID") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(MEMBER) }
            }
    }

    @Test
    internal fun `should retrieve a not found if member was not found`() {
        every { mockService.findMemberById(MEMBER_ID) } returns Optional.empty()

        mockMvc
            .get("$BASE_URL/$MEMBER_ID") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isNotFound() }
                content { string("") }
            }
    }

    @Test
    internal fun `should return ok on update`() {
        every { mockService.updateMember(MEMBER_ID, UPDATE_MEMBER) } returns Optional.of(MEMBER)

        mockMvc
            .put("$BASE_URL/$MEMBER_ID") {
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

        mockMvc
            .post(BASE_URL) {
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

        mockMvc
            .put("$BASE_URL/archive") {
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

        mockMvc
            .put("$BASE_URL/archive") {
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

        mockMvc
            .delete("$BASE_URL/$MEMBER_ID") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isNoContent() }
                content { string("") }
            }
    }

    @Test
    internal fun `should return the deleted members`() {
        every { mockService.findDeletedMembers(DELETED_PAGEABLE) } returns PageImpl(listOf(MEMBER), DELETED_PAGEABLE, 1)

        mockMvc
            .get("$BASE_URL/deleted") {
                param("page", "0")
                param("size", "20")
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                jsonPath("$.content[0].id") { value("$MEMBER_ID") }
            }
    }

    @Test
    internal fun `should restore a deleted member`() {
        every { mockService.restoreMember(MEMBER_ID) } returns Optional.of(MEMBER)

        mockMvc
            .post("$BASE_URL/$MEMBER_ID/restore") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(MEMBER)) }
            }
    }

    @Test
    internal fun `should answer 404 when there is no deleted member to restore`() {
        every { mockService.restoreMember(MEMBER_ID) } returns Optional.empty()

        mockMvc
            .post("$BASE_URL/$MEMBER_ID/restore") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpect { status { isNotFound() } }
    }

    @Test
    internal fun `should answer 409 when the restored member clashes with another`() {
        every { mockService.restoreMember(MEMBER_ID) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post("$BASE_URL/$MEMBER_ID/restore") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpect { status { isConflict() } }
    }

    @Test
    internal fun `should return the revisions of a member`() {
        every { mockService.findMemberRevisions(MEMBER_ID) } returns listOf(REVISION_INFO)

        mockMvc
            .get("$BASE_URL/$MEMBER_ID/revisions") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(listOf(REVISION_INFO))) }
            }
    }

    @Test
    internal fun `should return a member as it was at a revision`() {
        every { mockService.findMemberRevision(MEMBER_ID, REVISION) } returns Optional.of(MEMBER)

        mockMvc
            .get("$BASE_URL/$MEMBER_ID/revisions/$REVISION") {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(MEMBER)) }
            }
    }

    @Test
    internal fun `should restore a revision of a member`() {
        every { mockService.restoreMemberRevision(MEMBER_ID, REVISION) } returns Optional.of(MEMBER)

        mockMvc
            .post("$BASE_URL/$MEMBER_ID/revisions/$REVISION/restore") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isOk() }
                content { json(objectMapper.writeValueAsString(MEMBER)) }
            }
    }

    private companion object {
        private const val REVISION = 3L
        private val REVISION_INFO = RevisionInfo(REVISION, Instant.parse("2026-10-30T12:00:00Z"), "author", RevisionType.UPDATED)
        private val DELETED_PAGEABLE: Pageable = PageRequest.of(0, 20)
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
        private val UPDATE_MEMBER =
            UpdateMember(
                url = URL,
                name = NAME,
                nickname = NICKNAME,
                description = DESCRIPTION,
                joinedAt = JOINED_AT,
                role = ROLE,
                status = STATUS,
                archived = ARCHIVED,
            )
        private val MEMBER =
            Member(
                url = URL,
                name = NAME,
                id = MEMBER_ID,
                nickname = NICKNAME,
                description = DESCRIPTION,
                joinedAt = JOINED_AT,
                role = ROLE,
                status = STATUS,
                archived = ARCHIVED,
            )
        private val MEMBER_LIST = listOf(MEMBER)
    }
}
