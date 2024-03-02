package hu.bsstudio.bssweb.eventvideo.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.eventvideo.client.EventVideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.Video
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

class AddVideoToEventIntegrationTest(
    @Autowired private val client: EventVideoClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and add video to event`() {
        val eventEntity = eventRepository.save(DetailedEventEntity(url = "url", title = "title"))
        val videoEntity = videoRepository.save(DetailedVideoEntity(title = "title"))

        val actual = client.addVideoToEvent(eventEntity.id, videoEntity.id)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual DetailedEvent(
                id = eventEntity.id,
                url = eventEntity.url,
                title = eventEntity.title,
                description = eventEntity.description,
                date = eventEntity.dateFrom,
                visible = eventEntity.visible,
                videos = listOf(
                    Video(
                        id = videoEntity.id,
                        urls = videoEntity.urls,
                        title = videoEntity.title,
                        uploadedAt = videoEntity.uploadedAt,
                        visible = videoEntity.visible
                    )
                )
            )
        }
    }

    @Test
    fun `it should return 500 when event does not exist`() {
        val videoEntity = videoRepository.save(DetailedVideoEntity(title = "title"))

        shouldThrow<FeignException.InternalServerError> {
            client.addVideoToEvent(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                videoEntity.id
            )
        }
    }

    @Test
    fun `it should return 404 when video does not exist`() {
        val eventEntity = eventRepository.save(DetailedEventEntity(url = "url", title = "title"))

        shouldThrow<FeignException.InternalServerError> {
            client.addVideoToEvent(
                eventEntity.id,
                UUID.fromString("00000000-0000-0000-0000-000000000000")
            )
        }
    }
}
