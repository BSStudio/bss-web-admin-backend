package hu.bsstudio.bssweb.event.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.model.Event
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.time.LocalDate

class ReadEventIntegrationTest(
    @Autowired private val client: EventClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and empty list`() {
        val actual = client.findAllEvent()

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        actual.body.shouldBeEmpty()
    }

    @Test
    fun `it should return 200 and list with one item`() {
        val entity = eventRepository.save(DetailedEventEntity(url = URL, title = TITLE))

        val actual = client.findAllEvent()

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        actual.body.shouldContainExactly(
            Event(
                id = entity.id,
                url = URL,
                title = TITLE,
                description = "",
                date = LocalDate.now(),
                visible = false
            )
        )
    }

    private companion object {
        private const val URL = "url"
        private const val TITLE = "title"
    }
}
