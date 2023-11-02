package hu.bsstudio.bssweb.video.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.time.LocalDate
import java.util.UUID

class UpdateVideoIntegrationTest(
    @Autowired private val client: VideoClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and updated video`() {
        val entity = this.videoRepository.save(DetailedVideoEntity(url = "url", title = "title"))

        val actual = client.updateVideo(entity.id, UPDATE_VIDEO)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual DetailedVideo(
                id = entity.id,
                url = UPDATE_VIDEO.url,
                title = UPDATE_VIDEO.title,
                description = UPDATE_VIDEO.description,
                visible = UPDATE_VIDEO.visible,
                uploadedAt = UPDATE_VIDEO.uploadedAt,
                crew = listOf()
            )
        }
    }

    @Test
    fun `it should return 404 when video not found`() {
        shouldThrow<FeignException.NotFound> {
            client.updateVideo(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                UPDATE_VIDEO
            )
        }
    }

    private companion object {
        private val UPDATE_VIDEO = UpdateVideo(
            url = "updatedUrl",
            title = "updatedTitle",
            description = "updatedDescription",
            visible = true,
            uploadedAt = LocalDate.of(2023, 1, 1)
        )
    }
}
