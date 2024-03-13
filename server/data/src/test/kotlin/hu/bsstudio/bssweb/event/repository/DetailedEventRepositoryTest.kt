package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class DetailedEventRepositoryTest(
    @Autowired private val underTest: DetailedEventRepository,
    @Autowired private val simpleEventRepository: SimpleEventRepository,
    @Autowired private val simpleVideoRepository: SimpleVideoRepository,
    @Autowired private val eventVideoRepository: EventVideoRepository,
    @Autowired private val entityManager: TestEntityManager,
) {
    @Test
    internal fun `create read delete`() {
        underTest.count().shouldBeZero()

        val entity = DetailedEventEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id
        entityManager.run {
            flush()
            clear()
        }

        val expected = createExpected(id)
        underTest.findById(id) shouldBePresent {
            it.shouldBeEqualToIgnoringFields(expected, ::createdAt, ::updatedAt)
        }

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
    }

    @Test
    internal fun `create read delete with video`() {
        val eventId = simpleEventRepository.save(SimpleEventEntity(url = URL, title = TITLE)).id
        val video = simpleVideoRepository.save(SimpleVideoEntity(title = "title"))

        eventVideoRepository.save(EventVideoEntity(eventId = eventId, videoId = video.id))
        entityManager.run {
            flush()
            clear()
        }

        val actual = underTest.findById(eventId)

        val expected = createExpected(eventId, listOf(video))
        actual shouldBePresent {
            it.shouldBeEqualToIgnoringFields(expected, ::createdAt, ::updatedAt)
            it.createdAt.shouldBeCloseTo(expected.createdAt, duration = 1.minutes)
            it.updatedAt.shouldBeCloseTo(expected.updatedAt, duration = 1.minutes)
        }

        underTest.deleteById(eventId)
        underTest.findById(eventId).shouldBeEmpty()
        simpleVideoRepository.count().shouldBe(1L)
    }

    private fun createExpected(
        id: UUID,
        videos: List<SimpleVideoEntity> = emptyList(),
    ) = DetailedEventEntity(
        url = URL,
        title = TITLE,
        description = "",
        dateFrom = LocalDate.now(),
        visible = false,
    ).apply {
        this.id = id
        this.videos = videos
        this.createdAt = Instant.now()
        this.updatedAt = Instant.now()
    }

    private companion object {
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
