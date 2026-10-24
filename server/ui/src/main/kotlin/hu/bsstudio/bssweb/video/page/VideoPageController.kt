package hu.bsstudio.bssweb.video.page

import hu.bsstudio.bssweb.label.service.LabelService
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.ui.config.ConditionalOnAdminUi
import hu.bsstudio.bssweb.ui.flash.Flash
import hu.bsstudio.bssweb.ui.flash.flash
import hu.bsstudio.bssweb.ui.paging.PAGE_SIZE
import hu.bsstudio.bssweb.ui.paging.TableQuery
import hu.bsstudio.bssweb.ui.paging.TableSort
import hu.bsstudio.bssweb.ui.paging.backTo
import hu.bsstudio.bssweb.ui.paging.byText
import hu.bsstudio.bssweb.ui.paging.search
import hu.bsstudio.bssweb.ui.paging.toPage
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
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
import org.springframework.web.bind.annotation.DeleteMapping
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
@RequestMapping("/videos")
@Suppress("TooManyFunctions")
class VideoPageController(
    private val videoService: VideoService,
    private val labelService: LabelService,
    private val memberService: MemberService,
    private val videoCrewService: VideoCrewService,
) {
    @GetMapping
    fun list(
        @PageableDefault(size = PAGE_SIZE) pageable: Pageable,
        @RequestParam(defaultValue = "") q: String,
        htmx: HtmxRequest,
        model: Model,
    ): String {
        val page =
            if (q.isBlank()) {
                videoService.findAllVideos(SORT.page(pageable))
            } else {
                // the paged service cannot search, so a search goes through the whole list
                videoService
                    .findAllVideos()
                    .search(q) { listOf(it.title) + it.urls }
                    .let { SORT.sort(it, pageable) }
                    .toPage(pageable)
            }
        model["q"] = q
        model["page"] = page
        model["sorting"] = SORT.header(pageable)
        // htmx (search, sort, paging) replaces only the table and its footer
        return if (htmx.isHtmxRequest) "admin/video/list :: results" else "admin/video/list"
    }

    @PutMapping("/{videoId}/visibility")
    fun changeVisibility(
        @PathVariable videoId: UUID,
        @RequestParam visible: Boolean,
        model: Model,
    ): String {
        val changed =
            try {
                videoService.changeVideoVisibility(listOf(videoId), visible)
                true
            } catch (e: DataIntegrityViolationException) {
                if (!e.isVideoInHiddenEvent()) throw e
                model["inHiddenEvent"] = true
                false
            }
        val video = findVideo(videoId)
        model["id"] = video.id
        model["title"] = video.title
        model["visible"] = video.visible
        if (changed) model["toast"] = Flash(if (video.visible) "video.toast.shown" else "video.toast.hidden", video.title)
        return "admin/video/list :: visibility"
    }

    @PostMapping("/visibility")
    fun changeVisibilityOfSelected(
        @RequestParam(defaultValue = "") videoIds: List<UUID>,
        @RequestParam visible: Boolean,
        table: TableQuery,
        redirect: RedirectAttributes,
    ): String {
        if (videoIds.isEmpty()) {
            redirect.flash("video.flash.noneSelected", error = true)
        } else {
            try {
                val changed = videoService.changeVideoVisibility(videoIds, visible)
                redirect.flash(if (visible) "video.flash.published" else "video.flash.hidden", changed.size)
            } catch (e: DataIntegrityViolationException) {
                // one transaction: when one video can't be shown, none of them is
                if (!e.isVideoInHiddenEvent()) throw e
                redirect.flash("video.flash.inHiddenEvent", error = true)
            }
        }
        redirect.backTo(table)
        return REDIRECT_TO_LIST
    }

    @GetMapping("/new")
    fun newVideo(model: Model): String {
        model["form"] = NewVideoForm()
        return NEW_PAGE
    }

    @PostMapping
    fun create(
        @Valid @ModelAttribute("form") form: NewVideoForm,
        binding: BindingResult,
        redirect: RedirectAttributes,
    ): String {
        if (binding.hasErrors()) return NEW_PAGE
        val title = form.title.trim()
        return try {
            val video = videoService.insertVideo(CreateVideo(title))
            redirect.flash("video.flash.created", video.title)
            "redirect:/videos/${video.id}"
        } catch (_: DataIntegrityViolationException) {
            binding.rejectValue("title", "video.error.duplicateTitle")
            NEW_PAGE
        } catch (e: RestClientException) {
            // the video exists; only creating its folder on the file server failed
            logger.warn("File server failed after creating video {}", title, e)
            redirect.flash("video.flash.createdWithoutFiles", title, error = true)
            REDIRECT_TO_LIST
        }
    }

    @GetMapping("/{videoId}")
    fun edit(
        @PathVariable videoId: UUID,
        model: Model,
    ): String {
        val video = findVideo(videoId)
        model["form"] = VideoForm.from(video)
        return editPage(video, model)
    }

    @PostMapping("/{videoId}")
    fun update(
        @PathVariable videoId: UUID,
        @Valid @ModelAttribute("form") form: VideoForm,
        binding: BindingResult,
        model: Model,
        redirect: RedirectAttributes,
    ): String {
        // a value the form's inputs can't send (not a date, an unknown status) leaves no form to show again
        if (binding.target == null) throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if (binding.hasErrors()) return editPage(findVideo(videoId), model)
        return try {
            val video = videoService.updateVideo(videoId, form.toUpdateVideo()).orElseThrow(::notFound)
            redirect.flash("video.flash.saved", video.title)
            REDIRECT_TO_LIST
        } catch (e: DataIntegrityViolationException) {
            if (e.isVideoInHiddenEvent()) {
                binding.rejectValue("visible", "video.error.inHiddenEvent")
            } else {
                binding.reject("video.error.conflict")
            }
            editPage(findVideo(videoId), model)
        } catch (e: RestClientException) {
            // the changes are saved; only passing the URLs on to the file server failed
            logger.warn("File server failed after saving video {}", videoId, e)
            redirect.flash("video.flash.savedWithoutFiles", form.title.trim(), error = true)
            "redirect:/videos/$videoId"
        }
    }

    @PostMapping("/{videoId}/crew")
    fun addCrew(
        @PathVariable videoId: UUID,
        @RequestParam memberId: UUID,
        @RequestParam position: String,
        @RequestParam(defaultValue = "") newPosition: String,
        model: Model,
    ): String {
        val chosen = (if (position == NEW_POSITION) newPosition else position).trim()
        // the form keeps both from happening (required, maxlength); only a crafted request gets here
        if (chosen.isEmpty() || chosen.length > POSITION_LENGTH) throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        videoCrewService.addPosition(VideoCrewRequest(videoId, chosen, memberId))
        return crewFragment(findVideo(videoId), model)
    }

    @DeleteMapping("/{videoId}/crew")
    fun removeCrew(
        @PathVariable videoId: UUID,
        @RequestParam memberId: UUID,
        @RequestParam position: String,
        model: Model,
    ): String {
        videoCrewService.removePosition(VideoCrewRequest(videoId, position, memberId))
        return crewFragment(findVideo(videoId), model)
    }

    @PostMapping("/{videoId}/delete")
    fun delete(
        @PathVariable videoId: UUID,
        redirect: RedirectAttributes,
    ): String {
        val video = findVideo(videoId)
        return try {
            videoService.deleteVideoById(videoId)
            redirect.flash("video.flash.deleted", video.title)
            REDIRECT_TO_LIST
        } catch (_: DataIntegrityViolationException) {
            redirect.flash("video.flash.deleteBlocked", video.title, error = true)
            "redirect:/videos/$videoId"
        }
    }

    private fun findVideo(videoId: UUID): DetailedVideo = videoService.findVideoById(videoId).orElseThrow(::notFound)

    private fun editPage(
        video: DetailedVideo,
        model: Model,
    ): String {
        model["labels"] = labelService.findAllLabels().sortedBy { it.name }
        addCrewOptions(video, model)
        return "admin/video/edit"
    }

    private fun crewFragment(
        video: DetailedVideo,
        model: Model,
    ): String {
        addCrewOptions(video, model)
        // puts the cursor back on the member list, ready for the next crew member
        model["crewChanged"] = true
        return "admin/video/edit :: crew"
    }

    private fun addCrewOptions(
        video: DetailedVideo,
        model: Model,
    ) {
        model["video"] = video
        model["members"] = memberService.findAllMembers().sortedBy { it.name }
        model["positions"] = videoCrewService.getPositions().sortedWith(byText { it })
        model["newPosition"] = NEW_POSITION
    }

    private fun notFound() = ResponseStatusException(HttpStatus.NOT_FOUND)

    private companion object {
        private val logger = LoggerFactory.getLogger(VideoPageController::class.java)
        private const val REDIRECT_TO_LIST = "redirect:/videos"
        private const val NEW_PAGE = "admin/video/new"
        private const val POSITION_LENGTH = 250

        private val SORT =
            TableSort<Video>(
                Sort.by(Sort.Order.desc("shootingDateStart"), Sort.Order.asc("title")),
                mapOf(
                    "title" to byText { it.title },
                    "shootingDateStart" to compareBy { it.shootingDateStart },
                    "visible" to compareBy { it.visible },
                ),
            )

        // the position list's "New position…" option; the typed name comes as newPosition
        private const val NEW_POSITION = "+"
    }
}
