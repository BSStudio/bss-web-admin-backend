package hu.bsstudio.bssweb.eventvideo.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.eventvideo.client.EventVideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

class RemoveVideoFromEventIntegrationTest(
    @Autowired private val client: EventVideoClient
) : IntegrationTest() {

    @Test
    fun `it should return 204 and remove video from event`() {
        val videoEntity = videoRepository.save(DetailedVideoEntity(url = "url", title = "title"))
        val eventEntity = eventRepository.save(DetailedEventEntity(url = "url", title = "title"))
            .apply {
                this.videos = listOf(
                    SimpleVideoEntity(
                        url = videoEntity.url,
                        title = videoEntity.title
                    ).apply { this.id = videoEntity.id }
                )
            }

        val actual = client.removeVideoFromEvent(eventEntity.id, videoEntity.id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        actual.body!! shouldBeEqual DetailedEvent(
            id = eventEntity.id,
            url = eventEntity.url,
            title = eventEntity.title,
            description = eventEntity.description,
            date = eventEntity.date,
            visible = eventEntity.visible,
            videos = listOf()
        )
    }

    @Test
    fun `it should return 404 when event does not exist`() {
        val videoEntity = videoRepository.save(DetailedVideoEntity(url = "url", title = "title"))

        shouldThrow<FeignException.NotFound> {
            client.removeVideoFromEvent(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                videoEntity.id
            )
        }
    }

    @Test
    fun `it should return 200 when video does not exist`() {
        val eventEntity = eventRepository.save(DetailedEventEntity(url = "url", title = "title"))

        val actual = client.removeVideoFromEvent(
            eventEntity.id,
            UUID.fromString("00000000-0000-0000-0000-000000000000")
        )

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        actual.body!! shouldBeEqual DetailedEvent(
            id = eventEntity.id,
            url = eventEntity.url,
            title = eventEntity.title,
            description = eventEntity.description,
            date = eventEntity.date,
            visible = eventEntity.visible,
            videos = listOf()
        )
    }
}
