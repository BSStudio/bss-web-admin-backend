package hu.bsstudio.bssweb.event.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.model.Event
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body).isEmpty()
    }

    @Test
    fun `it should return 200 and list with one item`() {
        val entity = eventRepository.save(DetailedEventEntity(url = URL, title = TITLE))

        val actual = client.findAllEvent()

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body).containsExactly(
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
