package hu.bsstudio.bssweb.eventvideo.repository

import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.repository.VideoRepository
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class EventVideoRepositoryTest(
    @param:Autowired private val eventRepository: EventRepository,
    @param:Autowired private val videoRepository: VideoRepository,
    @param:Autowired private val underTest: EventVideoRepository,
) {
    @Test
    internal fun `create read delete`() {
        val videoId = videoRepository.save(VideoEntity(title = "title")).id
        val eventId = eventRepository.save(EventEntity(url = "url", title = "title")).id

        val entity = EventVideoEntity(eventId, videoId)
        underTest.save(entity)

        underTest.findById(entity) shouldBePresent { it shouldBeEqualToComparingFields entity }

        underTest.deleteById(entity)
        underTest.findById(entity).shouldBeEmpty()
        videoRepository.count().shouldBe(1L)
        eventRepository.count().shouldBe(1L)
    }
}
