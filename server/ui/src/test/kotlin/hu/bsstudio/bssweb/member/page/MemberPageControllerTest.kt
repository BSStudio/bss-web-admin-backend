package hu.bsstudio.bssweb.member.page

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.ui.config.UiSecurityConfig
import hu.bsstudio.bssweb.ui.flash.Flash
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.startsWith
import org.hamcrest.Matchers.stringContainsInOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.web.client.ResourceAccessException
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(MemberPageController::class)
@Import(UiSecurityConfig::class)
internal class MemberPageControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var service: MemberService

    @BeforeEach
    internal fun setUp() {
        every { service.findMemberById(ANNA.id) } returns Optional.of(ANNA)
    }

    @Test
    internal fun `should send only the table to an htmx request`() {
        every { service.findAllMembers() } returns listOf(ANNA)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                header("HX-Request", "true")
            }.andExpectAll {
                status { isOk() }
                content { string(startsWith("""<div id="results">""")) }
                content { string(not(containsString("<html"))) }
            }
    }

    @Test
    internal fun `should list active members by name, then archived ones`() {
        every { service.findAllMembers() } returns listOf(ARCHIVED, BENCE, ANNA)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/member/list") }
                content { string(stringContainsInOrder(ANNA.name, BENCE.name, ARCHIVED.name)) }
                content { string(containsString("<td>Member</td>")) }
                content { string(containsString("""href="/members?q=&amp;size=25&amp;sort=status,asc"""")) }
                content { string(containsString("""<option value="ALUMNI">Alumni</option>""")) }
                content { string(containsString("""<tr class="archived">""")) }
                content {
                    string(
                        containsString("""<span class="cds--pagination__text cds--pagination__items-count">1–3 of 3 items</span>"""),
                    )
                }
            }
    }

    @Test
    internal fun `should sort members by status in the order they go through them`() {
        val candidate = BENCE.copy(status = MemberStatus.MEMBER_CANDIDATE)
        val alumni = ARCHIVED.copy(status = MemberStatus.ALUMNI)
        every { service.findAllMembers() } returns listOf(alumni, ANNA, candidate)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("sort", "status,desc")
            }.andExpectAll {
                content { string(stringContainsInOrder(alumni.name, ANNA.name, candidate.name)) }
                content { string(containsString("""aria-sort="descending">""")) }
            }
    }

    @Test
    internal fun `should sort members by URL, joined date and name`() {
        every { service.findAllMembers() } returns listOf(ARCHIVED, BENCE, ANNA)

        listOf(
            "url,asc" to listOf(ARCHIVED, ANNA, BENCE),
            "joinedAt,desc" to listOf(ANNA, BENCE, ARCHIVED),
            "name,desc" to listOf(BENCE, ANNA, ARCHIVED),
        ).forEach { (sort, order) ->
            mockMvc
                .get(BASE_URL) {
                    with(oauth2Login())
                    param("sort", sort)
                }.andExpect {
                    content { string(stringContainsInOrder(order.map(Member::name))) }
                }
        }
    }

    @Test
    internal fun `should search members by name, nickname, URL and role`() {
        every { service.findAllMembers() } returns listOf(ARCHIVED, BENCE, ANNA)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("q", "Panni kovacs")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString(ANNA.name)) }
                content { string(not(containsString(BENCE.name))) }
                content {
                    string(
                        containsString("""<span class="cds--pagination__text cds--pagination__items-count">1–1 of 1 item</span>"""),
                    )
                }
            }
    }

    @Test
    internal fun `should say when no member matches the search`() {
        every { service.findAllMembers() } returns listOf(ANNA)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("q", "bence")
            }.andExpect {
                content { string(containsString("Nothing matches “bence”.")) }
            }
    }

    @Test
    internal fun `should say when there are no members`() {
        every { service.findAllMembers() } returns emptyList()

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
            }.andExpect {
                content { string(containsString("No members yet.")) }
            }
    }

    @Test
    internal fun `should archive one member and return only their cell`() {
        every { service.archiveMembers(listOf(ANNA.id), true) } returns listOf(ANNA.id)
        every { service.findMemberById(ANNA.id) } returns Optional.of(ANNA.copy(archived = true))

        mockMvc
            .put("$BASE_URL/${ANNA.id}/archived") {
                with(oauth2Login())
                with(csrf())
                param("archived", "true")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""hx-put="$BASE_URL/${ANNA.id}/archived?archived=false"""")) }
                content { string(containsString("""id="archived-${ANNA.id}"""")) }
                content { string(containsString("""role="switch"""")) }
                content { string(containsString("""<div hx-swap-oob="beforeend:#toasts">""")) }
                content { string(containsString("Archived ${ANNA.name}.")) }
                content { string(not(containsString("<html"))) }
            }
    }

    @Test
    internal fun `should say in a toast that a member is restored`() {
        every { service.archiveMembers(listOf(ANNA.id), false) } returns listOf(ANNA.id)
        every { service.findMemberById(ANNA.id) } returns Optional.of(ANNA.copy(archived = false))

        mockMvc
            .put("$BASE_URL/${ANNA.id}/archived") {
                with(oauth2Login())
                with(csrf())
                param("archived", "false")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""hx-put="$BASE_URL/${ANNA.id}/archived?archived=true"""")) }
                content { string(containsString("Restored ${ANNA.name}.")) }
            }
    }

    @Test
    internal fun `should archive selected members`() {
        every { service.archiveMembers(listOf(ANNA.id), true) } returns listOf(ANNA.id)

        mockMvc
            .post("$BASE_URL/archived") {
                with(oauth2Login())
                with(csrf())
                param("memberIds", ANNA.id.toString())
                param("archived", "true")
                param("q", "anna")
                param("page", "1")
                param("size", "10")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL?q=anna&page=1&size=10&sort=")
                flash { attribute("flash", Flash("member.flash.archived", 1)) }
            }
    }

    @Test
    internal fun `should restore selected members`() {
        every { service.archiveMembers(listOf(ANNA.id), false) } returns listOf(ANNA.id)

        mockMvc
            .post("$BASE_URL/archived") {
                with(oauth2Login())
                with(csrf())
                param("memberIds", ANNA.id.toString())
                param("archived", "false")
            }.andExpectAll {
                status { is3xxRedirection() }
                flash { attribute("flash", Flash("member.flash.restored", 1)) }
            }
    }

    @Test
    internal fun `should ask for a selection when nothing is selected`() {
        mockMvc
            .post("$BASE_URL/archived") {
                with(oauth2Login())
                with(csrf())
                param("archived", "true")
            }.andExpectAll {
                status { is3xxRedirection() }
                flash { attribute("flash", Flash("member.flash.noneSelected", error = true)) }
            }
    }

    @Test
    internal fun `should change the status of selected members`() {
        val memberIds = listOf(ANNA.id, BENCE.id)
        every { service.changeMemberStatus(memberIds, MemberStatus.ALUMNI) } returns memberIds

        mockMvc
            .post("$BASE_URL/status") {
                with(oauth2Login())
                with(csrf())
                param("memberIds", ANNA.id.toString(), BENCE.id.toString())
                param("status", "ALUMNI")
                param("page", "2")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL?q=&page=2&size=25&sort=")
                flash { attribute("flash", Flash("member.flash.statusChanged", 2)) }
            }
    }

    @Test
    internal fun `should ask for a selection before changing the status`() {
        mockMvc
            .post("$BASE_URL/status") {
                with(oauth2Login())
                with(csrf())
                param("status", "ALUMNI")
            }.andExpectAll {
                status { is3xxRedirection() }
                flash { attribute("flash", Flash("member.flash.noneSelected", error = true)) }
            }

        verify(exactly = 0) { service.changeMemberStatus(any(), any()) }
    }

    @Test
    internal fun `should show the new member form`() {
        mockMvc
            .get("$BASE_URL/new") {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/member/new") }
                model { attribute("form", NewMemberForm()) }
            }
    }

    @Test
    internal fun `should create a member and open them`() {
        every { service.insertMember(CreateMember(ANNA.url, ANNA.name)) } returns ANNA

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("url", " ${ANNA.url} ")
                param("name", " ${ANNA.name} ")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL/${ANNA.id}")
                flash { attribute("flash", Flash("member.flash.created", ANNA.name)) }
            }
    }

    @Test
    internal fun `should explain a duplicate URL when creating`() {
        every { service.insertMember(any()) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("url", ANNA.url)
                param("name", ANNA.name)
            }.andExpectAll {
                status { isOk() }
                content {
                    string(
                        containsString("""<small id="url-error" class="field-error">Another member already uses this URL.</small>"""),
                    )
                }
                content { string(containsString("""value="${ANNA.url}"""")) }
            }
    }

    @Test
    internal fun `should not create a member without a name`() {
        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("url", ANNA.url)
                param("name", " ")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<small id="name-error" class="field-error">Fill this in.</small>""")) }
            }

        verify(exactly = 0) { service.insertMember(any()) }
    }

    @Test
    internal fun `should say so when the file server fails after creating`() {
        every { service.insertMember(any()) } throws ResourceAccessException("connection refused")

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("url", ANNA.url)
                param("name", ANNA.name)
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("member.flash.createdWithoutFiles", ANNA.name, error = true)) }
            }
    }

    @Test
    internal fun `should fill the edit form`() {
        mockMvc
            .get("$BASE_URL/${ANNA.id}") {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/member/edit") }
                model { attribute("form", MemberForm.from(ANNA)) }
                content { string(containsString("""<option value="MEMBER" selected="selected">Member</option>""")) }
                content { string(stringContainsInOrder("Applicant", "Member candidate", "Member", "Active alumni", "Alumni")) }
            }
    }

    @Test
    internal fun `should save the form and return to the list`() {
        every { service.updateMember(ANNA.id, MemberForm.from(ANNA).toUpdateMember()) } returns Optional.of(ANNA)

        mockMvc
            .post("$BASE_URL/${ANNA.id}") {
                with(oauth2Login())
                with(csrf())
                formFields(" ${ANNA.name} ")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("member.flash.saved", ANNA.name)) }
            }

        verify { service.updateMember(ANNA.id, MemberForm.from(ANNA).toUpdateMember()) }
    }

    @Test
    internal fun `should not save a member without a status or a joining date`() {
        mockMvc
            .post("$BASE_URL/${ANNA.id}") {
                with(oauth2Login())
                with(csrf())
                param("name", ANNA.name)
                param("url", ANNA.url)
                param("joinedAt", "")
            }.andExpectAll {
                status { isOk() }
                view { name("admin/member/edit") }
                content { string(containsString("""<small id="status-error" class="field-error">Fill this in.</small>""")) }
                content { string(containsString("""<small id="joinedAt-error" class="field-error">Fill this in.</small>""")) }
            }

        verify(exactly = 0) { service.updateMember(any(), any()) }
    }

    @Test
    internal fun `should refuse a status that does not exist`() {
        mockMvc
            .post("$BASE_URL/${ANNA.id}") {
                with(oauth2Login())
                with(csrf())
                param("name", ANNA.name)
                param("url", ANNA.url)
                param("status", "PRESIDENT")
            }.andExpect {
                status { isBadRequest() }
            }

        verify(exactly = 0) { service.updateMember(any(), any()) }
    }

    @Test
    internal fun `should keep the form open when the URL is taken`() {
        every { service.updateMember(ANNA.id, any()) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post("$BASE_URL/${ANNA.id}") {
                with(oauth2Login())
                with(csrf())
                formFields(ANNA.name)
            }.andExpectAll {
                status { isOk() }
                view { name("admin/member/edit") }
                content {
                    string(
                        containsString("""<small id="url-error" class="field-error">Another member already uses this URL.</small>"""),
                    )
                }
                content { string(containsString("""id="url" required maxlength="250"""")) }
                content { string(containsString("""aria-describedby="url-help url-error" name="url"""")) }
            }
    }

    @Test
    internal fun `should say so when the file server fails after saving`() {
        every { service.updateMember(ANNA.id, any()) } throws ResourceAccessException("connection refused")

        mockMvc
            .post("$BASE_URL/${ANNA.id}") {
                with(oauth2Login())
                with(csrf())
                formFields(ANNA.name)
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL/${ANNA.id}")
                flash { attribute("flash", Flash("member.flash.savedWithoutFiles", ANNA.name, error = true)) }
            }
    }

    @Test
    internal fun `should answer not found when saving a deleted member`() {
        every { service.updateMember(ANNA.id, any()) } returns Optional.empty()

        mockMvc
            .post("$BASE_URL/${ANNA.id}") {
                with(oauth2Login())
                with(csrf())
                formFields(ANNA.name)
            }.andExpect {
                status { isNotFound() }
            }
    }

    @Test
    internal fun `should delete a member`() {
        every { service.removeMember(ANNA.id) } just runs

        mockMvc
            .post("$BASE_URL/${ANNA.id}/delete") {
                with(oauth2Login())
                with(csrf())
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("member.flash.deleted", ANNA.name)) }
            }
    }

    @Test
    internal fun `should keep a member who is still in a crew`() {
        every { service.removeMember(ANNA.id) } throws DataIntegrityViolationException("referenced")

        mockMvc
            .post("$BASE_URL/${ANNA.id}/delete") {
                with(oauth2Login())
                with(csrf())
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL/${ANNA.id}")
                flash { attribute("flash", Flash("member.flash.deleteBlocked", ANNA.name, error = true)) }
            }
    }

    @Test
    internal fun `should answer not found for an unknown member`() {
        val unknownId = UUID.randomUUID()
        every { service.findMemberById(unknownId) } returns Optional.empty()

        mockMvc
            .get("$BASE_URL/$unknownId") {
                with(oauth2Login())
            }.andExpect {
                status { isNotFound() }
            }
    }

    private fun org.springframework.test.web.servlet.MockHttpServletRequestDsl.formFields(name: String) {
        param("name", name)
        param("nickname", ANNA.nickname)
        param("url", ANNA.url)
        param("role", ANNA.role)
        param("status", ANNA.status.name)
        param("joinedAt", ANNA.joinedAt.toString())
        param("description", ANNA.description)
    }

    private companion object {
        private const val BASE_URL = "/members"
        private val ANNA =
            Member(
                id = UUID.randomUUID(),
                url = "kovacs-anna",
                name = "Kovács Anna",
                nickname = "Panni",
                description = "",
                joinedAt = LocalDate.of(2024, 9, 1),
                role = "Camera",
                status = MemberStatus.MEMBER,
                archived = false,
            )
        private val BENCE = ANNA.copy(id = UUID.randomUUID(), url = "nagy-bence", name = "Nagy Bence", nickname = "")
        private val ARCHIVED = ANNA.copy(id = UUID.randomUUID(), url = "abel", name = "Ábel Old", archived = true)
    }
}
