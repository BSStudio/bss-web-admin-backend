package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import java.time.LocalDate
import java.util.UUID

class ReadVideoIntegrationTest(
    @param:Autowired private val client: VideoClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 404`() {
        shouldThrow<HttpClientErrorException.NotFound> {
            client.getVideo(UUID.randomUUID())
        }
    }

    @Test
    internal fun `it should return 200 with video`() {
        val title = "title"
        val created = client.createVideo(CreateVideo(title)).body.shouldNotBeNull()

        val actual = client.getVideo(created.id)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual
                DetailedVideo(
                    id = created.id,
                    urls = emptyList(),
                    title = title,
                    description = "",
                    visible = false,
                    shootingDateStart = LocalDate.now(),
                    shootingDateEnd = LocalDate.now(),
                    crew = listOf(),
                    labels = listOf(),
                )
        }

        client.deleteVideo(created.id)
    }
}
