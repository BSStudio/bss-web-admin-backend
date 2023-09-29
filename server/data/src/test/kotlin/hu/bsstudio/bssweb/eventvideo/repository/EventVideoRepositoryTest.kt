package hu.bsstudio.bssweb.eventvideo.repository

import hu.bsstudio.bssweb.DataTest
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class EventVideoRepositoryTest(
    @Autowired private val eventRepository: SimpleEventRepository,
    @Autowired private val videoRepository: SimpleVideoRepository,
    @Autowired private val underTest: EventVideoRepository
) : DataTest() {

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
