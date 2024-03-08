package hu.bsstudio.bssweb.video.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.DetailedVideo
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.time.LocalDate
import java.util.UUID

class ReadVideoIntegrationTest(
    @Autowired private val client: VideoClient
) : IntegrationTest() {

    @Test
    fun `it should return 404`() {
        shouldThrow<FeignException.NotFound> {
            client.getVideo(ID)
        }
    }

    @Test
    fun `it should return 200 with video`() {
        val entity = videoRepository.save(DetailedVideoEntity(title = TITLE).apply { id = ID })

        val actual = client.getVideo(entity.id)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual DetailedVideo(
                id = entity.id,
                urls = emptyList(),
                title = TITLE,
                description = "",
                visible = false,
                shootingDateStart = LocalDate.now(),
                shootingDateEnd = LocalDate.now(),
                crew = listOf(),
                labels = listOf()
            )
        }
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val TITLE = "title"
    }
}
