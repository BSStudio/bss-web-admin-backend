package hu.bsstudio.bssweb.event.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.revision.model.RevisionType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpClientErrorException
import java.util.UUID

class RestoreEventIntegrationTest(
    @param:Autowired private val client: EventClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and the restored event`() {
        val id = create()
        client.deleteEvent(id)

        val actual = client.restoreEvent(id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        actual.body!!.title shouldBeEqual TITLE
        eventRepository.findById(id).shouldBePresent()
    }

    @Test
    internal fun `it should return 404 when no deleted event has the id`() {
        val id = create()

        shouldThrow<HttpClientErrorException.NotFound> { client.restoreEvent(id) }
    }

    @Test
    internal fun `it should return 409 when another event has taken its url`() {
        val id = create()
        client.deleteEvent(id)
        create()

        shouldThrow<HttpClientErrorException.Conflict> { client.restoreEvent(id) }
    }

    @Test
    internal fun `it should list the revisions and restore an earlier one`() {
        val id = create()
        val current = client.findEventById(id).body!!
        client.updateEvent(
            id,
            UpdateEvent(
                url = current.url,
                title = NEW_TITLE,
                description = current.description,
                dateFrom = current.dateFrom,
                dateTo = current.dateTo,
                visible = current.visible,
            ),
        )
        client.deleteEvent(id)
        client.restoreEvent(id)

        val revisions = client.getEventRevisions(id).body!!
        val first = revisions.last().number
        val actual = client.restoreEventRevision(id, first)

        revisions.map { it.type } shouldContainExactly
            listOf(RevisionType.RESTORED, RevisionType.DELETED, RevisionType.UPDATED, RevisionType.CREATED)
        client.getEventRevision(id, first).body!!.title shouldBeEqual TITLE
        actual.body!!.title shouldBeEqual TITLE
        client.findEventById(id).body!!.title shouldBeEqual TITLE
    }

    @Test
    internal fun `it should return 404 when the event to roll back is missing`() {
        shouldThrow<HttpClientErrorException.NotFound> {
            client.restoreEventRevision(UUID.fromString("00000000-0000-0000-0000-000000000000"), 1)
        }
    }

    private fun create() = client.createEvent(CreateEvent(url = URL, title = TITLE)).body!!.id

    private companion object {
        private const val URL = "url"
        private const val TITLE = "title"
        private const val NEW_TITLE = "newTitle"
    }
}
