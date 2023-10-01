package hu.bsstudio.bssweb.event.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.UpdateEvent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.time.LocalDate
import java.util.UUID

class UpdateEventIntegrationTest(
    @Autowired private val client: EventClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and updated event`() {
        val entity = this.eventRepository.save(DetailedEventEntity(url = "url", title = "title"))

        val actual = client.updateEvent(entity.id, UPDATE_EVENT)

        actual.body!! shouldBeEqual DetailedEvent(
            id = entity.id,
            url = UPDATE_EVENT.url,
            title = UPDATE_EVENT.title,
            description = UPDATE_EVENT.description,
            date = UPDATE_EVENT.date,
            visible = UPDATE_EVENT.visible,
            videos = listOf()
        )

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(200)
    }

    @Test
    fun `it should return 404 when event not found`() {
        shouldThrow<FeignException.NotFound> {
            client.updateEvent(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                UPDATE_EVENT
            )
        }
    }

    private companion object {
        private val UPDATE_EVENT = UpdateEvent(
            url = "updatedUrl",
            title = "updatedTitle",
            description = "updatedDescription",
            date = LocalDate.of(2023, 1, 1),
            visible = true
        )
    }
}
