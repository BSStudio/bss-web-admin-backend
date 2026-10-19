package hu.bsstudio.bssweb.event.page

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import hu.bsstudio.bssweb.ui.config.ConditionalOnAdminUi
import hu.bsstudio.bssweb.ui.flash.Flash
import hu.bsstudio.bssweb.ui.flash.flash
import hu.bsstudio.bssweb.ui.paging.PAGE_SIZE
import hu.bsstudio.bssweb.ui.paging.TableSort
import hu.bsstudio.bssweb.ui.paging.byText
import hu.bsstudio.bssweb.ui.paging.search
import hu.bsstudio.bssweb.ui.paging.toPage
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest
import jakarta.validation.Valid
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
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.UUID

@ConditionalOnAdminUi
@Controller
@RequestMapping("/admin/events")
@Suppress("TooManyFunctions")
class EventPageController(
    private val eventService: EventService,
    private val eventVideoService: EventVideoService,
    private val videoService: VideoService,
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
            eventService
                .findAllEvent()
                .search(q) { listOf(it.title, it.url) }
                .let { SORT.sort(it, pageable) }
                .toPage(pageable)
        model["sorting"] = SORT.header(pageable)
        // htmx (search, sort, paging) replaces only the table and its footer
        return if (htmx.isHtmxRequest) "admin/event/list :: results" else "admin/event/list"
    }

    @PutMapping("/{eventId}/visibility")
    fun changeVisibility(
        @PathVariable eventId: UUID,
        @RequestParam visible: Boolean,
        model: Model,
    ): String {
        eventService.changeEventVisibility(listOf(eventId), visible)
        val event = findEvent(eventId)
        model["id"] = event.id
        model["title"] = event.title
        model["visible"] = event.visible
        model["toast"] = Flash(if (event.visible) "event.toast.shown" else "event.toast.hidden", event.title)
        return "admin/event/list :: visibility"
    }

    @GetMapping("/new")
    fun newEvent(model: Model): String {
        model["form"] = NewEventForm()
        return NEW_PAGE
    }

    @PostMapping
    fun create(
        @Valid @ModelAttribute("form") form: NewEventForm,
        binding: BindingResult,
        redirect: RedirectAttributes,
    ): String {
        if (binding.hasErrors()) return NEW_PAGE
        return try {
            val event = eventService.insertEvent(CreateEvent(form.url.trim(), form.title.trim()))
            redirect.flash("event.flash.created", event.title)
            "redirect:/admin/events/${event.id}"
        } catch (_: DataIntegrityViolationException) {
            binding.reject("event.error.duplicate")
            NEW_PAGE
        }
    }

    @GetMapping("/{eventId}")
    fun edit(
        @PathVariable eventId: UUID,
        model: Model,
    ): String {
        val event = findEvent(eventId)
        model["form"] = EventForm.from(event)
        return editPage(event, model)
    }

    @PostMapping("/{eventId}")
    fun update(
        @PathVariable eventId: UUID,
        @Valid @ModelAttribute("form") form: EventForm,
        binding: BindingResult,
        model: Model,
        redirect: RedirectAttributes,
    ): String {
        // a value the form's inputs can't send (not a date, an unknown status) leaves no form to show again
        if (binding.target == null) throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if (binding.hasErrors()) return editPage(findEvent(eventId), model)
        return try {
            val event = eventService.updateEvent(eventId, form.toUpdateEvent()).orElseThrow(::notFound)
            redirect.flash("event.flash.saved", event.title)
            REDIRECT_TO_LIST
        } catch (_: DataIntegrityViolationException) {
            binding.reject("event.error.conflict")
            editPage(findEvent(eventId), model)
        }
    }

    @PostMapping("/{eventId}/videos")
    fun addVideo(
        @PathVariable eventId: UUID,
        @RequestParam videoId: UUID,
        model: Model,
    ): String = videosFragment(eventVideoService.addVideoToEvent(eventId, videoId).orElseThrow(::notFound), model)

    /** Up to [MATCHES] videos for the search, newest first, without the ones already in the event. */
    @GetMapping("/{eventId}/videos/search")
    fun searchVideos(
        @PathVariable eventId: UUID,
        @RequestParam(defaultValue = "") q: String,
        model: Model,
    ): String {
        val event = findEvent(eventId)
        val inEvent = event.videos.map { it.id }.toSet()
        model["event"] = event
        model["q"] = q
        model["videoMatches"] =
            videoService
                .findAllVideos()
                .search(q) { listOf(it.title) + it.urls }
                .filterNot { it.id in inEvent }
                .sortedByDescending { it.shootingDateStart }
                .take(if (q.isBlank()) 0 else MATCHES)
        return "admin/event/edit :: videoMatches"
    }

    @DeleteMapping("/{eventId}/videos")
    fun removeVideo(
        @PathVariable eventId: UUID,
        @RequestParam videoId: UUID,
        model: Model,
    ): String = videosFragment(eventVideoService.removeVideoFromEvent(eventId, videoId).orElseThrow(::notFound), model)

    @PostMapping("/{eventId}/delete")
    fun delete(
        @PathVariable eventId: UUID,
        redirect: RedirectAttributes,
    ): String {
        val event = findEvent(eventId)
        return try {
            eventService.removeEvent(eventId)
            redirect.flash("event.flash.deleted", event.title)
            REDIRECT_TO_LIST
        } catch (_: DataIntegrityViolationException) {
            redirect.flash("event.flash.deleteBlocked", event.title, error = true)
            "redirect:/admin/events/$eventId"
        }
    }

    private fun findEvent(eventId: UUID): DetailedEvent = eventService.findEventById(eventId).orElseThrow(::notFound)

    private fun editPage(
        event: DetailedEvent,
        model: Model,
    ): String {
        addVideos(event, model)
        return "admin/event/edit"
    }

    private fun videosFragment(
        event: DetailedEvent,
        model: Model,
    ): String {
        addVideos(event, model)
        // puts the cursor back in the video search, ready for the next one
        model["videosChanged"] = true
        return "admin/event/edit :: videos"
    }

    private fun addVideos(
        event: DetailedEvent,
        model: Model,
    ) {
        model["event"] = event
        model["eventVideos"] = event.videos.sortedBy { it.shootingDateStart }
        model["q"] = ""
        model["videoMatches"] = emptyList<Video>()
    }

    private fun notFound() = ResponseStatusException(HttpStatus.NOT_FOUND)

    private companion object {
        private const val MATCHES = 10
        private val SORT =
            TableSort<Event>(
                Sort.by(Sort.Order.desc("dateFrom"), Sort.Order.asc("title")),
                mapOf(
                    "title" to byText { it.title },
                    "url" to byText { it.url },
                    "dateFrom" to compareBy { it.dateFrom },
                    "videoCount" to compareBy { it.videoCount },
                    "visible" to compareBy { it.visible },
                ),
            )
        private const val REDIRECT_TO_LIST = "redirect:/admin/events"
        private const val NEW_PAGE = "admin/event/new"
    }
}
