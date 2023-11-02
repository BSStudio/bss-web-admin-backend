package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

class DeleteVideoIntegrationTest(
    @Autowired private val client: VideoClient
) : IntegrationTest() {

    @Test
    fun `it should return 204 and delete video`() {
        val entity = videoRepository.save(DetailedVideoEntity(url = "url", title = "title"))

        val actual = client.deleteVideo(entity.id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }

    @Test
    fun `it should return 204 when video not found`() {
        val actual = client.deleteVideo(UUID.fromString("00000000-0000-0000-0000-000000000000"))

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }
}
