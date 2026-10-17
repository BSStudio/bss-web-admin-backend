package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
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
import kotlin.time.Duration.Companion.minutes

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class EventRepositoryTest(
    @param:Autowired private val underTest: SimpleEventRepository,
    @param:Autowired private val videoRepository: SimpleVideoRepository,
    @param:Autowired private val eventVideoRepository: EventVideoRepository,
    @param:Autowired private val entityManager: TestEntityManager,
) {
    @Test
    internal fun `create read delete`() {
        underTest.count().shouldBeZero()

        val id = underTest.save(SimpleEventEntity(url = URL, title = TITLE)).id

        entityManager.flush()

        val actual = underTest.findById(id)

        val expected =
            SimpleEventEntity(
                url = URL,
                title = TITLE,
                description = "",
                dateFrom = LocalDate.now(),
                dateTo = LocalDate.now(),
                visible = false,
            ).apply {
                this.id = id
                this.createdAt = Instant.now()
                this.updatedAt = Instant.now()
            }
        actual shouldBePresent {
            it.shouldBeEqualToIgnoringFields(expected, ::createdAt, ::updatedAt)
            it.createdAt.shouldBeCloseTo(expected.createdAt, duration = 1.minutes)
            it.updatedAt.shouldBeCloseTo(expected.updatedAt, duration = 1.minutes)
        }

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
    }

    @Test
    internal fun `count the videos of each event when loading them`() {
        val eventId = underTest.save(SimpleEventEntity(url = URL, title = TITLE)).id
        val emptyId = underTest.save(SimpleEventEntity(url = "empty", title = "Empty")).id
        listOf("first", "second")
            .map { videoRepository.save(SimpleVideoEntity(title = it)).id }
            .forEach { eventVideoRepository.save(EventVideoEntity(eventId, it)) }
        entityManager.run {
            flush()
            clear()
        }

        underTest.findAll().associate { it.id to it.videoCount } shouldBe mapOf(eventId to 2L, emptyId to 0L)
    }

    @Test
    internal fun `change the visibility of events in one statement`() {
        val changed = underTest.save(SimpleEventEntity(url = URL, title = TITLE)).id
        val untouched = underTest.save(SimpleEventEntity(url = "other", title = "Other")).id
        entityManager.flush()
        val updatedAt = Instant.parse("2026-10-17T12:00:00Z")

        underTest.updateVisibility(listOf(changed), true, updatedAt) shouldBe 1

        underTest.findById(changed) shouldBePresent {
            it.visible shouldBe true
            it.updatedAt shouldBe updatedAt
            it.title shouldBe TITLE
        }
        underTest.findById(untouched) shouldBePresent { it.visible shouldBe false }
    }

    private companion object {
        private const val URL = "simonyi-konferencia"
        private const val TITLE = "Simonyi Konferencia"
    }
}
