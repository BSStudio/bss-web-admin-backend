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
        val entity = this.videoRepository.save(DetailedVideoEntity(title = "title"))

        val actual = client.updateVideo(entity.id, UPDATE_VIDEO)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual DetailedVideo(
                id = entity.id,
                urls = UPDATE_VIDEO.urls,
                title = UPDATE_VIDEO.title,
                description = UPDATE_VIDEO.description,
                visible = UPDATE_VIDEO.visible,
                shootingDateStart = UPDATE_VIDEO.shootingDateStart,
                shootingDateEnd = UPDATE_VIDEO.shootingDateEnd,
                crew = listOf(),
                categories = listOf()
            )
        }
    }

    @Test
    fun `it should return 500 when dateTo is before dateFrom`() {
        val entity = this.videoRepository.save(DetailedVideoEntity(title = "title"))

        val updateVideo = UPDATE_VIDEO.copy(shootingDateEnd = LocalDate.EPOCH)
        shouldThrow<FeignException.InternalServerError> {
            client.updateVideo(entity.id, updateVideo)
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
            urls = listOf("updatedUrl0", "updatedUrl1"),
            title = "updatedTitle",
            description = "updatedDescription",
            visible = true,
            shootingDateStart = LocalDate.of(2023, 1, 1),
            shootingDateEnd = LocalDate.of(2023, 1, 2),
            categories = listOf()
        )
    }
}
