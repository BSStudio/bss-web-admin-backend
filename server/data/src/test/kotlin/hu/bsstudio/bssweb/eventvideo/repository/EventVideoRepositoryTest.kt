package hu.bsstudio.bssweb.eventvideo.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@TestPropertySource(properties = ["spring.flyway.locations=classpath:db/migration/{vendor}"])
class EventVideoRepositoryTest(
    @Autowired private val eventRepository: SimpleEventRepository,
    @Autowired private val videoRepository: SimpleVideoRepository,
    @Autowired private val underTest: EventVideoRepository
) {

    @Test
    fun `create read delete`() {
        val videoId = videoRepository.save(SimpleVideoEntity(url = "url", title = "title")).id
        val eventId = eventRepository.save(SimpleEventEntity(url = "url", title = "title")).id

        val entity = EventVideoEntity(eventId, videoId)
        this.underTest.save(entity)

        assertThat(this.underTest.findById(entity))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(entity)

        this.underTest.deleteById(entity)
        assertThat(this.underTest.findById(entity)).isEmpty
        assertThat(videoRepository.count()).isOne
        assertThat(eventRepository.count()).isOne
        videoRepository.deleteById(videoId)
        eventRepository.deleteById(eventId)
    }
}
