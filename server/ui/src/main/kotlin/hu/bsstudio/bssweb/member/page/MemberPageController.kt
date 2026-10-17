package hu.bsstudio.bssweb.member.page

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.ui.config.ConditionalOnAdminUi
import hu.bsstudio.bssweb.ui.flash.flash
import hu.bsstudio.bssweb.ui.paging.PAGE_SIZE
import hu.bsstudio.bssweb.ui.paging.TableQuery
import hu.bsstudio.bssweb.ui.paging.TableSort
import hu.bsstudio.bssweb.ui.paging.backTo
import hu.bsstudio.bssweb.ui.paging.byText
import hu.bsstudio.bssweb.ui.paging.search
import hu.bsstudio.bssweb.ui.paging.toPage
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
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

@ConditionalOnAdminUi
@Controller
@RequestMapping("/admin/members")
@Suppress("TooManyFunctions")
class MemberPageController(
    private val memberService: MemberService,
) {
    @GetMapping
    fun list(
        @PageableDefault(size = PAGE_SIZE) pageable: Pageable,
        @RequestParam(defaultValue = "") q: String,
        htmx: HtmxRequest,
        model: Model,
    ): String {
        model["q"] = q
        model["page"] =
            memberService
                .findAllMembers()
                .search(q) { listOf(it.name, it.nickname, it.url, it.role) }
                .let { SORT.sort(it, pageable) }
                .toPage(pageable)
        model["sorting"] = SORT.header(pageable)
        model["statuses"] = STATUSES
        // htmx (search, sort, paging) replaces only the table and its footer
        return if (htmx.isHtmxRequest) "admin/member/list :: results" else "admin/member/list"
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
        table: TableQuery,
        redirect: RedirectAttributes,
    ): String {
        if (memberIds.isEmpty()) {
            redirect.flash("member.flash.noneSelected", error = true)
        } else {
            val changed = memberService.archiveMembers(memberIds, archived)
            redirect.flash(if (archived) "member.flash.archived" else "member.flash.restored", changed.size)
        }
        redirect.backTo(table)
        return REDIRECT_TO_LIST
    }

    @PostMapping("/status")
    fun changeStatusOfSelected(
        @RequestParam(defaultValue = "") memberIds: List<UUID>,
        @RequestParam status: MemberStatus,
        table: TableQuery,
        redirect: RedirectAttributes,
    ): String {
        if (memberIds.isEmpty()) {
            redirect.flash("member.flash.noneSelected", error = true)
        } else {
            val changed = memberService.changeMemberStatus(memberIds, status)
            redirect.flash("member.flash.statusChanged", changed.size)
        }
        redirect.backTo(table)
        return REDIRECT_TO_LIST
    }

    @GetMapping("/new")
    fun newMember(model: Model): String {
        model["form"] = NewMemberForm()
        return NEW_PAGE
    }

    @PostMapping
    fun create(
        @Valid @ModelAttribute("form") form: NewMemberForm,
        binding: BindingResult,
        redirect: RedirectAttributes,
    ): String {
        if (binding.hasErrors()) return NEW_PAGE
        return try {
            val member = memberService.insertMember(CreateMember(form.url.trim(), form.name.trim()))
            redirect.flash("member.flash.created", member.name)
            "redirect:/admin/members/${member.id}"
        } catch (_: DataIntegrityViolationException) {
            binding.rejectValue("url", "member.error.duplicateUrl")
            NEW_PAGE
        } catch (e: RestClientException) {
            // the member exists; only creating their folder on the file server failed
            logger.warn("File server failed after creating member {}", form.url, e)
            redirect.flash("member.flash.createdWithoutFiles", form.name.trim(), error = true)
            REDIRECT_TO_LIST
        }
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
        @Valid @ModelAttribute("form") form: MemberForm,
        binding: BindingResult,
        model: Model,
        redirect: RedirectAttributes,
    ): String {
        // a value the form's inputs can't send (not a date, an unknown status) leaves no form to show again
        if (binding.target == null) throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if (binding.hasErrors()) return editPage(findMember(memberId), model)
        return try {
            val member = memberService.updateMember(memberId, form.toUpdateMember()).orElseThrow(::notFound)
            redirect.flash("member.flash.saved", member.name)
            REDIRECT_TO_LIST
        } catch (_: DataIntegrityViolationException) {
            binding.rejectValue("url", "member.error.duplicateUrl")
            editPage(findMember(memberId), model)
        } catch (e: RestClientException) {
            // the changes are saved; only passing the URL on to the file server failed
            logger.warn("File server failed after saving member {}", memberId, e)
            redirect.flash("member.flash.savedWithoutFiles", form.name.trim(), error = true)
            "redirect:/admin/members/$memberId"
        }
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
            REDIRECT_TO_LIST
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
        model["statuses"] = STATUSES
        return "admin/member/edit"
    }

    private fun notFound() = ResponseStatusException(HttpStatus.NOT_FOUND)

    private companion object {
        private val logger = LoggerFactory.getLogger(MemberPageController::class.java)
        private const val REDIRECT_TO_LIST = "redirect:/admin/members"
        private const val NEW_PAGE = "admin/member/new"

        // in the order a member goes through them
        private val STATUSES = MemberStatus.entries.reversed()

        private val SORT =
            TableSort<Member>(
                Sort.by("archived", "name"),
                mapOf(
                    "name" to byText { it.name },
                    "url" to byText { it.url },
                    "status" to compareBy { STATUSES.indexOf(it.status) },
                    "joinedAt" to compareBy { it.joinedAt },
                    "archived" to compareBy { it.archived },
                ),
            )
    }
}
