package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.repository.VideoRepository
import io.kotest.matchers.date.shouldBeCloseTo
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class EventRepositoryTest(
    @param:Autowired private val underTest: EventRepository,
    @param:Autowired private val videoRepository: VideoRepository,
    @param:Autowired private val eventVideoRepository: EventVideoRepository,
    @param:Autowired private val entityManager: TestEntityManager,
) {
    @Test
    internal fun `create read delete`() {
        underTest.count().shouldBeZero()

        val id = underTest.save(EventEntity(url = URL, title = TITLE)).id
        entityManager.run {
            flush()
            clear()
        }

        val expected = createExpected(id)
        underTest.findById(id) shouldBePresent {
            it.shouldBeEqualToIgnoringFields(expected, EventEntity::createdAt, EventEntity::updatedAt)
            it.createdAt.shouldBeCloseTo(expected.createdAt, duration = 1.minutes)
            it.updatedAt.shouldBeCloseTo(expected.updatedAt, duration = 1.minutes)
        }

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
    }

    @Test
    internal fun `create read delete with video`() {
        val eventId = underTest.save(EventEntity(url = URL, title = TITLE)).id
        val video = videoRepository.save(VideoEntity(title = "title"))
        eventVideoRepository.save(EventVideoEntity(eventId = eventId, videoId = video.id))
        entityManager.run {
            flush()
            clear()
        }

        val expected = createExpected(eventId, listOf(video))
        underTest.findById(eventId) shouldBePresent {
            it.shouldBeEqualToIgnoringFields(expected, EventEntity::createdAt, EventEntity::updatedAt)
            it.videoCount shouldBe 1L
        }

        underTest.deleteById(eventId)
        underTest.findById(eventId).shouldBeEmpty()
        videoRepository.count().shouldBe(1L)
    }

    @Test
    internal fun `count the videos of each event when loading them`() {
        val eventId = underTest.save(EventEntity(url = URL, title = TITLE)).id
        val emptyId = underTest.save(EventEntity(url = "empty", title = "Empty")).id
        listOf("first", "second")
            .map { videoRepository.save(VideoEntity(title = it)).id }
            .forEach { eventVideoRepository.save(EventVideoEntity(eventId, it)) }
        entityManager.run {
            flush()
            clear()
        }

        underTest.findAll().associate { it.id to it.videoCount } shouldBe mapOf(eventId to 2L, emptyId to 0L)
    }

    private fun createExpected(
        id: UUID,
        videos: List<VideoEntity> = emptyList(),
    ) = EventEntity(
        url = URL,
        title = TITLE,
        description = "",
        dateFrom = LocalDate.now(),
        dateTo = LocalDate.now(),
        visible = false,
    ).apply {
        this.id = id
        this.videos = videos
        this.videoCount = videos.size.toLong()
        this.createdAt = Instant.now()
        this.updatedAt = Instant.now()
    }

    private companion object {
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
