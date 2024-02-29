package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.Video
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class ReadAllVideoIntegrationTest(
    @Autowired private val client: VideoClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and empty list`() {
        val actual = client.getAllVideos()

        assertSoftly(actual) {
            body.shouldBeEmpty()
            statusCode shouldBeEqual org.springframework.http.HttpStatusCode.valueOf(200)
        }
    }

    @Test
    fun `it should return 200 and list with 1 video`() {
        val entity = videoRepository.save(DetailedVideoEntity(title = TITLE))

        val actual = client.getAllVideos()

        assertSoftly(actual) {
            body.shouldContainExactly(
                Video(
                    id = entity.id,
                    urls = emptyList(),
                    title = TITLE,
                    visible = false,
                    uploadedAt = LocalDate.now()
                )
            )
            statusCode shouldBeEqual org.springframework.http.HttpStatusCode.valueOf(200)
        }
    }

    private companion object {
        private const val TITLE = "title"
    }
}
