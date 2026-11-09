package hu.bsstudio.bssweb.event.visibility

import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.dao.DataIntegrityViolationException
import java.util.UUID

/** The database keeps a video hidden while any of its events is hidden (V9__hide_videos_of_hidden_events.sql). */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class HiddenEventVideosTest(
    @param:Autowired private val events: SimpleEventRepository,
    @param:Autowired private val videos: SimpleVideoRepository,
    @param:Autowired private val eventVideos: EventVideoRepository,
    @param:Autowired private val entityManager: TestEntityManager,
) {
    @Test
    internal fun `should hide the videos of an event that gets hidden`() {
        val event = events.save(SimpleEventEntity(url = "camp", title = "Camp", visible = true))
        val inEvent = video("In the event", event.id)
        val elsewhere = video("Elsewhere")
        reload()
        val before = videos.findById(inEvent).get().updatedAt

        events.save(events.findById(event.id).get().apply { visible = false })
        reload()

        videos.findById(inEvent).get().run {
            visible shouldBe false
            updatedAt shouldBeAfter before
        }
        videos.findById(elsewhere).get().visible shouldBe true
    }

    @Test
    internal fun `should leave the videos hidden when the event is shown again`() {
        val event = events.save(SimpleEventEntity(url = "camp", title = "Camp", visible = true))
        val inEvent = video("In the event", event.id)
        reload()
        events.save(events.findById(event.id).get().apply { visible = false })
        reload()

        events.save(events.findById(event.id).get().apply { visible = true })
        reload()

        videos.findById(inEvent).get().visible shouldBe false
    }

    @Test
    internal fun `should hide a video added to a hidden event`() {
        val hidden = events.save(SimpleEventEntity(url = "camp", title = "Camp", visible = false))
        val shown = events.save(SimpleEventEntity(url = "ball", title = "Ball", visible = true))
        val toHidden = video("Added to the hidden event")
        val toShown = video("Added to the visible event")
        reload()

        eventVideos.save(EventVideoEntity(hidden.id, toHidden))
        eventVideos.save(EventVideoEntity(shown.id, toShown))
        reload()

        videos.findById(toHidden).get().visible shouldBe false
        videos.findById(toShown).get().visible shouldBe true
    }

    @Test
    internal fun `should refuse to show a video that is in a hidden event`() {
        val shown = events.save(SimpleEventEntity(url = "ball", title = "Ball", visible = true))
        val hidden = events.save(SimpleEventEntity(url = "camp", title = "Camp", visible = false))
        val video = videos.save(SimpleVideoEntity(title = "In both")).id
        eventVideos.save(EventVideoEntity(shown.id, video))
        eventVideos.save(EventVideoEntity(hidden.id, video))
        reload()

        val error =
            shouldThrow<DataIntegrityViolationException> {
                videos.saveAndFlush(videos.findById(video).get().apply { visible = true })
            }

        error.mostSpecificCause.message shouldContain "video_in_hidden_event"
    }

    @Test
    internal fun `should show a video once all its events are visible`() {
        val event = events.save(SimpleEventEntity(url = "ball", title = "Ball", visible = true))
        val video = videos.save(SimpleVideoEntity(title = "Ball")).id
        eventVideos.save(EventVideoEntity(event.id, video))
        reload()

        videos.saveAndFlush(videos.findById(video).get().apply { visible = true })
        reload()

        videos.findById(video).get().visible shouldBe true
    }

    private fun video(
        title: String,
        eventId: UUID? = null,
    ): UUID {
        val id = videos.save(SimpleVideoEntity(title = title, visible = true)).id
        eventId?.let { eventVideos.save(EventVideoEntity(it, id)) }
        return id
    }

    /** Writes the changes, so the triggers run, and forgets the loaded entities, so the next read sees their result. */
    private fun reload() =
        entityManager.run {
            flush()
            clear()
        }
}
