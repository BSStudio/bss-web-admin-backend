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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = [
    "spring.datasource.url=jdbc:tc:postgresql:15.4-alpine:///databasename"
])
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
        underTest.save(entity)

        assertThat(underTest.findById(entity))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(entity)

        underTest.deleteById(entity)
        assertThat(underTest.findById(entity)).isEmpty()
        assertThat(videoRepository.count()).isOne()
        assertThat(eventRepository.count()).isOne()
    }
}
