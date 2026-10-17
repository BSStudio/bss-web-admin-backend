package hu.bsstudio.bssweb.video.page

import hu.bsstudio.bssweb.label.service.LabelService
import hu.bsstudio.bssweb.media.service.MediaService
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.ui.flash.flash
import hu.bsstudio.bssweb.ui.media.uploadImage
import hu.bsstudio.bssweb.ui.paging.PAGE_SIZE
import hu.bsstudio.bssweb.ui.paging.TableQuery
import hu.bsstudio.bssweb.ui.paging.backTo
import hu.bsstudio.bssweb.ui.paging.search
import hu.bsstudio.bssweb.ui.paging.toPage
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestClientException
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.UUID

@Controller
@RequestMapping("/admin/videos")
@Suppress("TooManyFunctions")
class VideoPageController(
    private val videoService: VideoService,
    private val labelService: LabelService,
    private val memberService: MemberService,
    private val videoCrewService: VideoCrewService,
    private val mediaService: MediaService,
) {
    @GetMapping
    fun list(
        @PageableDefault(size = PAGE_SIZE)
        @SortDefault.SortDefaults(
            SortDefault(sort = ["shootingDateStart"], direction = Sort.Direction.DESC),
            SortDefault(sort = ["title"]),
        )
        pageable: Pageable,
        @RequestParam(defaultValue = "") q: String,
        model: Model,
    ): String {
        model["q"] = q
        model["page"] =
            if (q.isBlank()) {
                videoService.findAllVideos(pageable)
            } else {
                // the paged service cannot search, so a search goes through the whole list
                videoService
                    .findAllVideos()
                    .search(q) { listOf(it.title) + it.urls }
                    .sortedWith(compareByDescending<Video> { it.shootingDateStart }.thenBy { it.title })
                    .toPage(pageable)
            }
        return "admin/video/list"
    }

    @PutMapping("/{videoId}/visibility")
    fun changeVisibility(
        @PathVariable videoId: UUID,
        @RequestParam visible: Boolean,
        model: Model,
    ): String {
        videoService.changeVideoVisibility(listOf(videoId), visible)
        val video = findVideo(videoId)
        model["id"] = video.id
        model["title"] = video.title
        model["visible"] = video.visible
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
            val changed = videoService.changeVideoVisibility(videoIds, visible)
            redirect.flash(if (visible) "video.flash.published" else "video.flash.hidden", changed.size)
        }
        redirect.backTo(table)
        return REDIRECT_TO_LIST
    }

    @GetMapping("/new")
    fun newVideo(): String = "admin/video/new"

    @PostMapping
    fun create(
        @RequestParam title: String,
        model: Model,
        redirect: RedirectAttributes,
    ): String =
        try {
            val video = videoService.insertVideo(CreateVideo(title.trim()))
            redirect.flash("video.flash.created", video.title)
            "redirect:/admin/videos/${video.id}"
        } catch (_: DataIntegrityViolationException) {
            model["title"] = title
            model["error"] = "video.error.duplicateTitle"
            "admin/video/new"
        } catch (e: RestClientException) {
            // the video exists; only creating its folder on the file server failed
            logger.warn("File server failed after creating video {}", title, e)
            redirect.flash("video.flash.createdWithoutFiles", title.trim(), error = true)
            REDIRECT_TO_LIST
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
        @ModelAttribute("form") form: VideoForm,
        model: Model,
        redirect: RedirectAttributes,
    ): String =
        try {
            val video = videoService.updateVideo(videoId, form.toUpdateVideo()).orElseThrow(::notFound)
            redirect.flash("video.flash.saved", video.title)
            REDIRECT_TO_LIST
        } catch (_: DataIntegrityViolationException) {
            model["error"] = "video.error.conflict"
            editPage(findVideo(videoId), model)
        } catch (e: RestClientException) {
            // the changes are saved; only passing the URLs on to the file server failed
            logger.warn("File server failed after saving video {}", videoId, e)
            redirect.flash("video.flash.savedWithoutFiles", form.title.trim(), error = true)
            "redirect:/admin/videos/$videoId"
        }

    @PostMapping("/{videoId}/crew")
    fun addCrew(
        @PathVariable videoId: UUID,
        @RequestParam memberId: UUID,
        @RequestParam position: String,
        model: Model,
    ): String {
        videoCrewService.addPosition(VideoCrewRequest(videoId, position.trim(), memberId))
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

    @PostMapping("/{videoId}/thumbnail")
    fun uploadThumbnail(
        @PathVariable videoId: UUID,
        @RequestParam image: MultipartFile,
        model: Model,
    ): String {
        model["video"] = findVideo(videoId)
        uploadImage(model, "video.thumbnail.uploaded") { mediaService.uploadVideoThumbnail(videoId, image) }
        return "admin/video/edit :: thumbnail"
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
            "redirect:/admin/videos/$videoId"
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
        model["positions"] = videoCrewService.getPositions()
    }

    private fun notFound() = ResponseStatusException(HttpStatus.NOT_FOUND)

    private companion object {
        private val logger = LoggerFactory.getLogger(VideoPageController::class.java)
        private const val REDIRECT_TO_LIST = "redirect:/admin/videos"
    }
}
