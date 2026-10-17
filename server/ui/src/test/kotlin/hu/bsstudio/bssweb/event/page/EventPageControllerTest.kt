package hu.bsstudio.bssweb.event.page

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import hu.bsstudio.bssweb.ui.config.UiSecurityConfig
import hu.bsstudio.bssweb.ui.flash.Flash
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.stringContainsInOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@WebMvcTest(EventPageController::class)
@Import(UiSecurityConfig::class)
internal class EventPageControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var eventService: EventService

    @MockkBean
    private lateinit var eventVideoService: EventVideoService

    @MockkBean
    private lateinit var videoService: VideoService

    @BeforeEach
    internal fun setUp() {
        every { eventService.findEventById(CAMP.id) } returns Optional.of(CAMP)
        every { videoService.findAllVideos() } returns listOf(ARRIVAL, CUSTOMS, OPEN_DAY)
    }

    @Test
    internal fun `should list events newest first`() {
        every { eventService.findAllEvent() } returns listOf(OLD_EVENT, EVENT)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/event/list") }
                content { string(stringContainsInOrder(EVENT.title, OLD_EVENT.title)) }
                content { string(containsString("2026-08-12 – 2026-08-15")) }
                content { string(containsString("""<td class="date col-date">2025-05-01</td>""")) }
            }
    }

    @Test
    internal fun `should search events by title and URL`() {
        every { eventService.findAllEvent() } returns listOf(OLD_EVENT, EVENT)

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
                param("q", "qpa-2025")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString(OLD_EVENT.title)) }
                content { string(not(containsString(EVENT.title))) }
            }
    }

    @Test
    internal fun `should say when there are no events`() {
        every { eventService.findAllEvent() } returns emptyList()

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
            }.andExpect {
                content { string(containsString("No events yet.")) }
            }
    }

    @Test
    internal fun `should change visibility of one event and return only its cell`() {
        val update = EventForm.from(CAMP).copy(visible = false).toUpdateEvent()
        every { eventService.updateEvent(CAMP.id, update) } returns Optional.of(CAMP.copy(visible = false))

        mockMvc
            .put("$BASE_URL/${CAMP.id}/visibility") {
                with(oauth2Login())
                with(csrf())
                param("visible", "false")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""hx-put="$BASE_URL/${CAMP.id}/visibility?visible=true"""")) }
                content { string(not(containsString("<html"))) }
            }
    }

    @Test
    internal fun `should show the new event form`() {
        mockMvc
            .get("$BASE_URL/new") {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/event/new") }
            }
    }

    @Test
    internal fun `should create an event and open it`() {
        every { eventService.insertEvent(CreateEvent(EVENT.url, EVENT.title)) } returns EVENT

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("url", " ${EVENT.url} ")
                param("title", " ${EVENT.title} ")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL/${EVENT.id}")
                flash { attribute("flash", Flash("event.flash.created", EVENT.title)) }
            }
    }

    @Test
    internal fun `should explain a duplicate when creating`() {
        every { eventService.insertEvent(any()) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("url", EVENT.url)
                param("title", EVENT.title)
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("Another event already uses this title or URL.")) }
                content { string(containsString("""value="${EVENT.url}"""")) }
            }
    }

    @Test
    internal fun `should fill the edit form and offer only videos outside the event`() {
        mockMvc
            .get("$BASE_URL/${CAMP.id}") {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/event/edit") }
                model { attribute("form", EventForm.from(CAMP)) }
                content { string(stringContainsInOrder(ARRIVAL.title, CUSTOMS.title, "Choose a video", OPEN_DAY.title)) }
                content { string(not(containsString("""value="${ARRIVAL.id}""""))) }
                content { string(not(containsString("""autofocus="autofocus""""))) }
            }
    }

    @Test
    internal fun `should save the form and return to the list`() {
        every { eventService.updateEvent(CAMP.id, EventForm.from(CAMP).toUpdateEvent()) } returns Optional.of(CAMP)

        mockMvc
            .post("$BASE_URL/${CAMP.id}") {
                with(oauth2Login())
                with(csrf())
                formFields(" ${CAMP.title} ")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("event.flash.saved", CAMP.title)) }
            }

        verify { eventService.updateEvent(CAMP.id, EventForm.from(CAMP).toUpdateEvent()) }
    }

    @Test
    internal fun `should keep the form open when saving conflicts`() {
        every { eventService.updateEvent(CAMP.id, any()) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post("$BASE_URL/${CAMP.id}") {
                with(oauth2Login())
                with(csrf())
                formFields(CAMP.title, visible = false)
            }.andExpectAll {
                status { isOk() }
                view { name("admin/event/edit") }
                content { string(containsString("Could not save.")) }
                content { string(containsString("""data-unsaved="pending">""")) }
            }
    }

    @Test
    internal fun `should answer not found when saving a deleted event`() {
        every { eventService.updateEvent(CAMP.id, any()) } returns Optional.empty()

        mockMvc
            .post("$BASE_URL/${CAMP.id}") {
                with(oauth2Login())
                with(csrf())
                formFields(CAMP.title)
            }.andExpect {
                status { isNotFound() }
            }
    }

    @Test
    internal fun `should add a video and return only the videos panel`() {
        val withOpenDay = CAMP.copy(videos = CAMP.videos + OPEN_DAY)
        every { eventVideoService.addVideoToEvent(CAMP.id, OPEN_DAY.id) } returns Optional.of(withOpenDay)

        mockMvc
            .post("$BASE_URL/${CAMP.id}/videos") {
                with(oauth2Login())
                with(csrf())
                param("videoId", OPEN_DAY.id.toString())
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""<section id="event-videos" class="panel">""")) }
                content { string(containsString("""autofocus="autofocus"""")) }
                content { string(not(containsString("<html"))) }
            }
    }

    @Test
    internal fun `should remove a video`() {
        every { eventVideoService.removeVideoFromEvent(CAMP.id, ARRIVAL.id) } returns Optional.of(CAMP.copy(videos = listOf(CUSTOMS)))

        mockMvc
            .delete("$BASE_URL/${CAMP.id}/videos") {
                with(oauth2Login())
                with(csrf())
                param("videoId", ARRIVAL.id.toString())
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("""value="${ARRIVAL.id}"""")) }
            }
    }

    @Test
    internal fun `should say when the event has no videos`() {
        every { eventVideoService.removeVideoFromEvent(CAMP.id, ARRIVAL.id) } returns Optional.of(CAMP.copy(videos = emptyList()))

        mockMvc
            .delete("$BASE_URL/${CAMP.id}/videos") {
                with(oauth2Login())
                with(csrf())
                param("videoId", ARRIVAL.id.toString())
            }.andExpect {
                content { string(containsString("No videos in this event yet.")) }
            }
    }

    @Test
    internal fun `should delete an event`() {
        every { eventService.removeEvent(CAMP.id) } just runs

        mockMvc
            .post("$BASE_URL/${CAMP.id}/delete") {
                with(oauth2Login())
                with(csrf())
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("event.flash.deleted", CAMP.title)) }
            }
    }

    @Test
    internal fun `should keep an event that still has videos`() {
        every { eventService.removeEvent(CAMP.id) } throws DataIntegrityViolationException("referenced")

        mockMvc
            .post("$BASE_URL/${CAMP.id}/delete") {
                with(oauth2Login())
                with(csrf())
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl("$BASE_URL/${CAMP.id}")
                flash { attribute("flash", Flash("event.flash.deleteBlocked", CAMP.title, error = true)) }
            }
    }

    @Test
    internal fun `should answer not found when the event disappears while changing visibility`() {
        every { eventService.updateEvent(CAMP.id, any()) } returns Optional.empty()

        mockMvc
            .put("$BASE_URL/${CAMP.id}/visibility") {
                with(oauth2Login())
                with(csrf())
                param("visible", "false")
            }.andExpect {
                status { isNotFound() }
            }
    }

    @Test
    internal fun `should answer not found when adding a video to a deleted event`() {
        every { eventVideoService.addVideoToEvent(CAMP.id, OPEN_DAY.id) } returns Optional.empty()

        mockMvc
            .post("$BASE_URL/${CAMP.id}/videos") {
                with(oauth2Login())
                with(csrf())
                param("videoId", OPEN_DAY.id.toString())
            }.andExpect {
                status { isNotFound() }
            }
    }

    @Test
    internal fun `should answer not found when removing a video from a deleted event`() {
        every { eventVideoService.removeVideoFromEvent(CAMP.id, ARRIVAL.id) } returns Optional.empty()

        mockMvc
            .delete("$BASE_URL/${CAMP.id}/videos") {
                with(oauth2Login())
                with(csrf())
                param("videoId", ARRIVAL.id.toString())
            }.andExpect {
                status { isNotFound() }
            }
    }

    @Test
    internal fun `should answer not found for an unknown event`() {
        val unknownId = UUID.randomUUID()
        every { eventService.findEventById(unknownId) } returns Optional.empty()

        mockMvc
            .get("$BASE_URL/$unknownId") {
                with(oauth2Login())
            }.andExpect {
                status { isNotFound() }
            }
    }

    private fun MockHttpServletRequestDsl.formFields(
        title: String,
        visible: Boolean = true,
    ) {
        param("title", title)
        param("url", CAMP.url)
        param("dateFrom", CAMP.dateFrom.toString())
        param("dateTo", CAMP.dateTo.toString())
        param("description", CAMP.description)
        // browsers leave unchecked checkboxes out of the form
        if (visible) param("visible", "true")
    }

    private companion object {
        private const val BASE_URL = "/admin/events"
        private val ARRIVAL = video("Gólyatábor 2026 - Megérkezés", LocalDate.of(2026, 8, 12))
        private val CUSTOMS = video("Gólyatábor 2026 - Vám", LocalDate.of(2026, 8, 13))
        private val OPEN_DAY = video("BME Nyílt nap 2025", LocalDate.of(2025, 11, 14))
        private val EVENT =
            Event(
                id = UUID.randomUUID(),
                url = "golyatabor-2026",
                title = "Gólyatábor 2026",
                description = "",
                dateFrom = LocalDate.of(2026, 8, 12),
                dateTo = LocalDate.of(2026, 8, 15),
                visible = true,
            )
        private val OLD_EVENT =
            EVENT.copy(
                id = UUID.randomUUID(),
                url = "qpa-2025",
                title = "Schönherz Qpa 2025",
                dateFrom = LocalDate.of(2025, 5, 1),
                dateTo = LocalDate.of(2025, 5, 1),
            )
        private val CAMP =
            DetailedEvent(
                id = EVENT.id,
                url = EVENT.url,
                title = EVENT.title,
                description = "Túlélő zóna",
                dateFrom = EVENT.dateFrom,
                dateTo = EVENT.dateTo,
                visible = true,
                videos = listOf(CUSTOMS, ARRIVAL),
            )

        private fun video(
            title: String,
            shot: LocalDate,
        ) = Video(UUID.randomUUID(), title, emptyList(), "", shot, shot, true)
    }
}
