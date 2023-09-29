package hu.bsstudio.bssweb.event.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.Event
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import java.net.URI
import java.time.LocalDate

class CreateEventIntegrationTest(
    @Autowired private val client: EventClient,
    @Value("\${bss.client.url}") private val url: String
) : IntegrationTest() {

    @Test
    fun `it should return 201 and event`() {
        val actual = client.createEvent(CREATE_EVENT)

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(201))
        assertThat(actual.body).isEqualTo(
            Event(
                id = actual.body!!.id,
                url = CREATE_EVENT.url,
                title = CREATE_EVENT.title,
                description = "",
                date = LocalDate.now(),
                visible = false
            )
        )
        assertThat(actual.headers.location).isEqualTo(URI.create("$url/api/v1/event/${actual.body!!.id}"))
    }

    @Test
    fun `it should retun 500 when duplicate urls were specified`() {
        eventRepository.save(DetailedEventEntity(url = CREATE_EVENT.url, title = CREATE_EVENT.title))

        assertThatExceptionOfType(FeignException.InternalServerError::class.java)
            .isThrownBy { client.createEvent(CREATE_EVENT) }
            .satisfies({ assertThat(it.contentUTF8()).contains(""","status":500,"error":"Internal Server Error","path":"/api/v1/event"}""") })
    }

    private companion object {
        private val CREATE_EVENT = CreateEvent(
            url = "url",
            title = "title"
        )
    }
}
