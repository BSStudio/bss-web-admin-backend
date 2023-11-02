package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.find
import hu.bsstudio.bssweb.persistAndGetId
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
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
    @Autowired private val entityManager: TestEntityManager
) {

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
        val eventId = entityManager.persistAndGetId<UUID>(SimpleEventEntity(url = URL, title = TITLE))
        val video = entityManager.persist(SimpleVideoEntity(url = "url", title = "title"))
        entityManager.persistAndFlush(EventVideoEntity(eventId = eventId, videoId = video.id))

        val actual = underTest.findById(eventId)
        val expected = createExpected(eventId, listOf(video))
        actual shouldBePresent { it shouldBeEqualToComparingFields expected }

        underTest.deleteById(eventId)
        entityManager.flush()

        underTest.findById(eventId).shouldBeEmpty()
        entityManager.find<SimpleVideoEntity>(video.id).shouldNotBeNull()
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
