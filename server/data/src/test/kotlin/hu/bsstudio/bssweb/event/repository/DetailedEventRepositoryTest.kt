package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDate
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DetailedEventRepositoryTest(
    @Autowired private val underTest: DetailedEventRepository,
    @Autowired private val simpleEventRepository: SimpleEventRepository,
    @Autowired private val simpleVideoRepository: SimpleVideoRepository,
    @Autowired private val eventVideoRepository: EventVideoRepository,
    @Autowired private val entityManager: TestEntityManager
) {

    @AfterEach
    fun tearDown() {
        entityManager.flush()
    }

    @Test
    internal fun `create read delete`() {
        underTest.count().shouldBeZero()

        val entity = DetailedEventEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id
        entityManager.run { flush(); clear() }

        val expected = createExpected(id)
        underTest.findById(id) shouldBePresent { it shouldBeEqualToComparingFields expected }

        underTest.deleteById(id)
        entityManager.flush()

        underTest.findById(id).shouldBeEmpty()
    }

    @Test
    internal fun `create read delete with video`() {
        val eventId = simpleEventRepository.save(SimpleEventEntity(url = URL, title = TITLE)).id
        val video = simpleVideoRepository.save(SimpleVideoEntity(url = "url", title = "title"))

        eventVideoRepository.save(EventVideoEntity(eventId = eventId, videoId = video.id))
        entityManager.run { flush(); clear() }

        val actual = underTest.findById(eventId)
        val expected = createExpected(eventId, listOf(video))
        actual shouldBePresent { it shouldBeEqualToComparingFields expected }

        underTest.deleteById(eventId)
        entityManager.flush()

        underTest.findById(eventId).shouldBeEmpty()
        simpleVideoRepository.findById(video.id).shouldBePresent()
    }

    private fun createExpected(id: UUID, videos: List<SimpleVideoEntity> = emptyList()) =
        DetailedEventEntity(
            url = URL,
            title = TITLE,
            description = "",
            date = LocalDate.now(),
            visible = false
        ).apply {
            this.id = id
            this.videos = videos
        }

    private companion object {
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
