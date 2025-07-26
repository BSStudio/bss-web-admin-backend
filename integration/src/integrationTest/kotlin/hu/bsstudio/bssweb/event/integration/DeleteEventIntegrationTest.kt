package hu.bsstudio.bssweb.event.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

internal class DeleteEventIntegrationTest(
    @Autowired private val client: EventClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 204 and delete event`() {
        val entity = eventRepository.save(DetailedEventEntity(url = "url", title = "title"))

        val actual = client.deleteEvent(entity.id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }

    @Test
    internal fun `it should return 204 when event not found`() {
        val actual = client.deleteEvent(UUID.fromString("00000000-0000-0000-0000-000000000000"))

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }
}
