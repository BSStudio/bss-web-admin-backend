package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@TestPropertySource(properties = ["spring.flyway.locations=classpath:db/migration/{vendor}"])
class DetailedEventRepositoryTest(
    @Autowired private val underTest: DetailedEventRepository,
    @Autowired private val simpleVideoRepository: SimpleVideoRepository,
    @Autowired private val eventVideoRepository: EventVideoRepository
) {

    @Test
    internal fun `create read delete`() {
        assertThat(this.underTest.count()).isZero

        val entity = DetailedEventEntity(url = URL, title = TITLE)
        this.underTest.save(entity)

        val id = entity.id
        val expected = DetailedEventEntity(
            url = URL,
            title = TITLE,
            description = "",
            date = LocalDate.now(),
            visible = false
        ).apply {
            this.id = id
            this.videos = emptyList()
        }
        assertThat(this.underTest.findById(id)).hasValue(expected)

        this.underTest.deleteById(id)
        assertThat(this.underTest.findById(id)).isEmpty
    }

/*
    @Test
    internal fun `create read delete with video`() {
        val id = DetailedEventEntity(url = URL, title = TITLE)
            .let { this.underTest.save(it) }
            .id

        val video = this.simpleVideoRepository.save(SimpleVideoEntity(url = "url", title = "title"))
        this.eventVideoRepository.saveAndFlush(EventVideoEntity(eventId = id, videoId = video.id))

        val expected = DetailedEventEntity(
            url = URL,
            title = TITLE,
            description = "",
            date = LocalDate.now(),
            visible = false
        ).apply {
            this.id = id
            this.videos = listOf(video)
        }
        assertThat(this.underTest.findById(id))
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expected)

        this.underTest.deleteById(id)
        assertThat(this.underTest.findById(id)).isEmpty
        assertThat(this.simpleVideoRepository.count()).isOne
        this.simpleVideoRepository.deleteById(video.id)
    }
*/
    private companion object {
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
