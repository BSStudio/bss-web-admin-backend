package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate
import java.util.UUID

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = ["spring.datasource.url=jdbc:tc:postgresql:15.4-alpine:///db"])
class DetailedEventRepositoryTest(
    @Autowired private val underTest: DetailedEventRepository,
    @Autowired private val simpleEventRepository: SimpleEventRepository,
    @Autowired private val simpleVideoRepository: SimpleVideoRepository,
    @Autowired private val eventVideoRepository: EventVideoRepository,
    @Autowired private val entityManager: TestEntityManager
) {

    @Test
    internal fun `create read delete`() {
        assertThat(underTest.count()).isZero

        val entity = DetailedEventEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id
        entityManager.run { flush(); clear() }

        val expected = createExpected(id)
        assertThat(underTest.findById(id))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expected)

        underTest.deleteById(id)
        assertThat(underTest.findById(id)).isEmpty()
    }

    @Test
    internal fun `create read delete with video`() {
        val eventId = simpleEventRepository.save(SimpleEventEntity(url = URL, title = TITLE)).id
        val video = simpleVideoRepository.save(SimpleVideoEntity(url = "url", title = "title"))

        eventVideoRepository.save(EventVideoEntity(eventId = eventId, videoId = video.id))
        entityManager.run { flush(); clear() }

        val actual = underTest.findById(eventId).orElseThrow()
        val expected = createExpected(eventId, listOf(video))
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected)

        underTest.deleteById(eventId)
        assertThat(underTest.findById(eventId)).isEmpty()
        assertThat(simpleVideoRepository.count()).isOne()
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
