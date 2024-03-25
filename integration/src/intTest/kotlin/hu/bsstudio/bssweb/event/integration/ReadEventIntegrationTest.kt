package hu.bsstudio.bssweb.event.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.model.DetailedEvent
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.time.LocalDate
import java.util.UUID

internal class ReadEventIntegrationTest(
    @Autowired private val client: EventClient
) : IntegrationTest() {

    @Test
    internal fun `it should return 404`() {
        assertThrows<FeignException.NotFound> {
            client.findEventById(ID)
        }
    }

    @Test
    internal fun `it should return 200 and event`() {
        val entity = eventRepository.save(DetailedEventEntity(url = URL, title = TITLE).apply { id = ID })

        val actual = client.findEventById(entity.id)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual DetailedEvent(
                id = entity.id,
                url = URL,
                title = TITLE,
                description = "",
                dateFrom = LocalDate.now(),
                dateTo = LocalDate.now(),
                visible = false,
                videos = listOf()
            )
        }
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val URL = "url"
        private const val TITLE = "title"
    }
}
