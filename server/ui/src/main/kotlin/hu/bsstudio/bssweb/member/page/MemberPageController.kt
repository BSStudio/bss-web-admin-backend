package hu.bsstudio.bssweb.member.page

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.ui.flash.flash
import hu.bsstudio.bssweb.ui.paging.toPage
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestClientException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.UUID

@Controller
@RequestMapping("/admin/members")
class MemberPageController(
    private val memberService: MemberService,
) {
    @GetMapping
    fun list(
        @PageableDefault(size = PAGE_SIZE) pageable: Pageable,
        model: Model,
    ): String {
        model["page"] =
            memberService
                .findAllMembers()
                .sortedWith(compareBy<Member> { it.archived }.thenBy { it.name.lowercase() })
                .toPage(pageable)
        return "admin/member/list"
    }

    @PutMapping("/{memberId}/archived")
    fun changeArchived(
        @PathVariable memberId: UUID,
        @RequestParam archived: Boolean,
        model: Model,
    ): String {
        memberService.archiveMembers(listOf(memberId), archived)
        val member = findMember(memberId)
        model["id"] = member.id
        model["name"] = member.name
        model["archived"] = member.archived
        return "admin/member/list :: archived"
    }

    @PostMapping("/archived")
    fun changeArchivedOfSelected(
        @RequestParam(defaultValue = "") memberIds: List<UUID>,
        @RequestParam archived: Boolean,
        @RequestParam(defaultValue = "0") page: Int,
        redirect: RedirectAttributes,
    ): String {
        if (memberIds.isEmpty()) {
            redirect.flash("member.flash.noneSelected", error = true)
        } else {
            val changed = memberService.archiveMembers(memberIds, archived)
            redirect.flash(if (archived) "member.flash.archived" else "member.flash.restored", changed.size)
        }
        return "redirect:/admin/members?page=$page"
    }

    @GetMapping("/new")
    fun newMember(): String = "admin/member/new"

    @PostMapping
    fun create(
        @RequestParam url: String,
        @RequestParam name: String,
        model: Model,
        redirect: RedirectAttributes,
    ): String =
        try {
            val member = memberService.insertMember(CreateMember(url.trim(), name.trim()))
            redirect.flash("member.flash.created", member.name)
            "redirect:/admin/members/${member.id}"
        } catch (_: DataIntegrityViolationException) {
            model["url"] = url
            model["name"] = name
            model["error"] = "member.error.duplicateUrl"
            "admin/member/new"
        } catch (e: RestClientException) {
            // the member exists; only creating their folder on the file server failed
            logger.warn("File server failed after creating member {}", url, e)
            redirect.flash("member.flash.createdWithoutFiles", name.trim(), error = true)
            "redirect:/admin/members"
        }

    @GetMapping("/{memberId}")
    fun edit(
        @PathVariable memberId: UUID,
        model: Model,
    ): String {
        val member = findMember(memberId)
        model["form"] = MemberForm.from(member)
        return editPage(member, model)
    }

    @PostMapping("/{memberId}")
    fun update(
        @PathVariable memberId: UUID,
        @ModelAttribute("form") form: MemberForm,
        model: Model,
        redirect: RedirectAttributes,
    ): String =
        try {
            val member = memberService.updateMember(memberId, form.toUpdateMember()).orElseThrow(::notFound)
            redirect.flash("member.flash.saved", member.name)
            "redirect:/admin/members"
        } catch (_: DataIntegrityViolationException) {
            model["error"] = "member.error.duplicateUrl"
            editPage(findMember(memberId), model)
        } catch (e: RestClientException) {
            // the changes are saved; only passing the URL on to the file server failed
            logger.warn("File server failed after saving member {}", memberId, e)
            redirect.flash("member.flash.savedWithoutFiles", form.name.trim(), error = true)
            "redirect:/admin/members/$memberId"
        }

    @PostMapping("/{memberId}/delete")
    fun delete(
        @PathVariable memberId: UUID,
        redirect: RedirectAttributes,
    ): String {
        val member = findMember(memberId)
        return try {
            memberService.removeMember(memberId)
            redirect.flash("member.flash.deleted", member.name)
            "redirect:/admin/members"
        } catch (_: DataIntegrityViolationException) {
            redirect.flash("member.flash.deleteBlocked", member.name, error = true)
            "redirect:/admin/members/$memberId"
        }
    }

    private fun findMember(memberId: UUID): Member = memberService.findMemberById(memberId).orElseThrow(::notFound)

    private fun editPage(
        member: Member,
        model: Model,
    ): String {
        model["member"] = member
        // in the order a member goes through them
        model["statuses"] = MemberStatus.entries.reversed()
        return "admin/member/edit"
    }

    private fun notFound() = ResponseStatusException(HttpStatus.NOT_FOUND)

    private companion object {
        private val logger = LoggerFactory.getLogger(MemberPageController::class.java)
        private const val PAGE_SIZE = 25
    }
}
