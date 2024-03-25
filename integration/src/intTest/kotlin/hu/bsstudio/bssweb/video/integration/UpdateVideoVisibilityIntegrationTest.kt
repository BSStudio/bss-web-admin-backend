package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode

class UpdateVideoVisibilityIntegrationTest(
    @Autowired private val client: VideoClient
) : IntegrationTest() {

    @Test
    internal fun `it should return 200 and updated ids`() {
        val entities = videoRepository.saveAll(
            listOf(
                DetailedVideoEntity(title = TITLE_1),
                DetailedVideoEntity(title = TITLE_2)
            )
        )

        val actual = client.changeVideoVisibility(entities.map { it.id }, true)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual entities.map { it.id }
        }
    }

    @Test
    internal fun `it should return 200 and updated ids wheen videos are being hidden`() {
        val entities = videoRepository.saveAll(
            listOf(
                DetailedVideoEntity(title = TITLE_1, visible = true),
                DetailedVideoEntity(title = TITLE_2, visible = true)
            )
        )

        val actual = client.changeVideoVisibility(entities.map { it.id }, false)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual entities.map { it.id }
        }
    }

    @Test
    internal fun `it should return 200 and empty list`() {
        val actual = client.changeVideoVisibility(listOf(), true)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!!.shouldBeEmpty()
        }
    }

    private companion object {
        private const val TITLE_1 = "title1"
        private const val TITLE_2 = "title2"
    }
}
