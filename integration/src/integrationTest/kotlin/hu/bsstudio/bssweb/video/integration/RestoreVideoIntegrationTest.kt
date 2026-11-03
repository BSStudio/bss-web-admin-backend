package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.revision.model.RevisionType
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpClientErrorException
import java.util.UUID

class RestoreVideoIntegrationTest(
    @param:Autowired private val client: VideoClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and the restored video`() {
        val id = create()
        client.deleteVideo(id)

        val actual = client.restoreVideo(id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        actual.body!!.title shouldBeEqual TITLE
        videoRepository.findById(id).shouldBePresent()
    }

    @Test
    internal fun `it should return 404 when no deleted video has the id`() {
        val id = create()

        shouldThrow<HttpClientErrorException.NotFound> { client.restoreVideo(id) }
    }

    @Test
    internal fun `it should return 409 when another video has taken its title`() {
        val id = create()
        client.deleteVideo(id)
        create()

        shouldThrow<HttpClientErrorException.Conflict> { client.restoreVideo(id) }
    }

    @Test
    internal fun `it should list the revisions and restore an earlier one`() {
        val id = create()
        val current = client.getVideo(id).body!!
        client.updateVideo(
            id,
            UpdateVideo(
                title = NEW_TITLE,
                urls = current.urls,
                description = current.description,
                shootingDateStart = current.shootingDateStart,
                shootingDateEnd = current.shootingDateEnd,
                visible = current.visible,
                labels = current.labels,
            ),
        )
        client.deleteVideo(id)
        client.restoreVideo(id)

        val revisions = client.getVideoRevisions(id).body!!
        val first = revisions.last().number
        val actual = client.restoreVideoRevision(id, first)

        revisions.map { it.type } shouldContainExactly
            listOf(RevisionType.RESTORED, RevisionType.DELETED, RevisionType.UPDATED, RevisionType.CREATED)
        client.getVideoRevision(id, first).body!!.title shouldBeEqual TITLE
        actual.body!!.title shouldBeEqual TITLE
        client.getVideo(id).body!!.title shouldBeEqual TITLE
    }

    @Test
    internal fun `it should return 404 when the video to roll back is missing`() {
        shouldThrow<HttpClientErrorException.NotFound> {
            client.restoreVideoRevision(UUID.fromString("00000000-0000-0000-0000-000000000000"), 1)
        }
    }

    private fun create() = client.createVideo(CreateVideo(title = TITLE)).body!!.id

    private companion object {
        private const val TITLE = "title"
        private const val NEW_TITLE = "newTitle"
    }
}
