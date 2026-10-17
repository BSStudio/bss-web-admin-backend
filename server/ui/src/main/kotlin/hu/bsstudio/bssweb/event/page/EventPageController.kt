package hu.bsstudio.bssweb.event.page

import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import hu.bsstudio.bssweb.ui.flash.flash
import hu.bsstudio.bssweb.ui.paging.toPage
import hu.bsstudio.bssweb.video.service.VideoService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.UUID

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
        model: Model,
    ): String {
        model["page"] =
            eventService
                .findAllEvent()
                .sortedWith(compareByDescending<Event> { it.dateFrom }.thenBy { it.title.lowercase() })
                .toPage(pageable)
        return "admin/event/list"
    }

    @PutMapping("/{eventId}/visibility")
    fun changeVisibility(
        @PathVariable eventId: UUID,
        @RequestParam visible: Boolean,
        model: Model,
    ): String {
        val event = findEvent(eventId)
        val update = EventForm.from(event).copy(visible = visible).toUpdateEvent()
        val updated = eventService.updateEvent(eventId, update).orElseThrow(::notFound)
        model["id"] = updated.id
        model["title"] = updated.title
        model["visible"] = updated.visible
        return "admin/event/list :: visibility"
    }

    @GetMapping("/new")
    fun newEvent(): String = "admin/event/new"

    @PostMapping
    fun create(
        @RequestParam url: String,
        @RequestParam title: String,
        model: Model,
        redirect: RedirectAttributes,
    ): String =
        try {
            val event = eventService.insertEvent(CreateEvent(url.trim(), title.trim()))
            redirect.flash("event.flash.created", event.title)
            "redirect:/admin/events/${event.id}"
        } catch (_: DataIntegrityViolationException) {
            model["url"] = url
            model["title"] = title
            model["error"] = "event.error.duplicate"
            "admin/event/new"
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
        @ModelAttribute("form") form: EventForm,
        model: Model,
        redirect: RedirectAttributes,
    ): String =
        try {
            val event = eventService.updateEvent(eventId, form.toUpdateEvent()).orElseThrow(::notFound)
            redirect.flash("event.flash.saved", event.title)
            REDIRECT_TO_LIST
        } catch (_: DataIntegrityViolationException) {
            model["error"] = "event.error.conflict"
            editPage(findEvent(eventId), model)
        }

    @PostMapping("/{eventId}/videos")
    fun addVideo(
        @PathVariable eventId: UUID,
        @RequestParam videoId: UUID,
        model: Model,
    ): String = videosFragment(eventVideoService.addVideoToEvent(eventId, videoId).orElseThrow(::notFound), model)

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
        // puts the cursor back on the video list, ready for the next one
        model["videosChanged"] = true
        return "admin/event/edit :: videos"
    }

    private fun addVideos(
        event: DetailedEvent,
        model: Model,
    ) {
        val inEvent = event.videos.map { it.id }.toSet()
        model["event"] = event
        model["eventVideos"] = event.videos.sortedBy { it.shootingDateStart }
        model["videoOptions"] =
            videoService
                .findAllVideos()
                .filterNot { it.id in inEvent }
                .sortedByDescending { it.shootingDateStart }
    }

    private fun notFound() = ResponseStatusException(HttpStatus.NOT_FOUND)

    private companion object {
        private const val PAGE_SIZE = 25
        private const val REDIRECT_TO_LIST = "redirect:/admin/events"
    }
}
