package hu.bsstudio.bssweb.event.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.Event
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import java.net.URI
import java.time.LocalDate

class CreateEventIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var client: EventClient
    @Value("\${bss.client.url}")
    private lateinit var url: String

    @Test
    fun `it should return 201 and event`() {
        val actual = client.createEvent(CREATE_EVENT)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(201)
            body!! shouldBeEqual Event(
                id = actual.body!!.id,
                url = CREATE_EVENT.url,
                title = CREATE_EVENT.title,
                description = "",
                dateFrom = LocalDate.now(),
                visible = false
            )
            headers.location!! shouldBeEqual URI.create("$url/api/v1/event/${actual.body!!.id}")
        }
    }

    @Test
    fun `it should retun 500 when duplicate urls were specified`() {
        eventRepository.save(DetailedEventEntity(url = CREATE_EVENT.url, title = CREATE_EVENT.title))

        shouldThrow<FeignException.InternalServerError> {
            client.createEvent(CREATE_EVENT)
        } should { it.contentUTF8() shouldContain ""","status":500,"error":"Internal Server Error"""" }
    }

    private companion object {
        private val CREATE_EVENT = CreateEvent(
            url = "url",
            title = "title"
        )
    }
}
