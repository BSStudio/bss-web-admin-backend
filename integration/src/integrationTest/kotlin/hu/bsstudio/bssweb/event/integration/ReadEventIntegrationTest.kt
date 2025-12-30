package hu.bsstudio.bssweb.event.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpClientErrorException
import java.time.LocalDate
import java.util.UUID

internal class ReadEventIntegrationTest(
    @param:Autowired private val client: EventClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 404`() {
        assertThrows<HttpClientErrorException.NotFound> {
            client.findEventById(UUID.randomUUID())
        }
    }

    @Test
    internal fun `it should return 200 and event`() {
        val url = "url"
        val title = "title"
        val created = client.createEvent(CreateEvent(url = url, title = title)).body.shouldNotBeNull()

        val actual = client.findEventById(created.id)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual
                DetailedEvent(
                    id = created.id,
                    url = url,
                    title = title,
                    description = "",
                    dateFrom = LocalDate.now(),
                    dateTo = LocalDate.now(),
                    visible = false,
                    videos = listOf(),
                )
        }

        client.deleteEvent(created.id)
    }
}
