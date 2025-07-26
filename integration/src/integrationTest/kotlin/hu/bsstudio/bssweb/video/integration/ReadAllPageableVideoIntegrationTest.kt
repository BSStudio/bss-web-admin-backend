package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.Video
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatusCode
import java.time.LocalDate

class ReadAllPageableVideoIntegrationTest(
    @Autowired private val client: VideoClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and paged`() {
        val (created0, created1, created2, created3) =
            IntRange(0, 3)
                .map { i ->
                    client.createVideo(CreateVideo(title = "title$i")).body.shouldNotBeNull()
                }.toList()

        val actual1 = client.getAllVideos(PageRequest.of(0, 2, Sort.by("title")))
        val actual2 = client.getAllVideos(PageRequest.of(1, 2, Sort.by("title")))

        assertSoftly(actual1) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!!.content.shouldContainExactly(
                Video(
                    id = created0.id,
                    title = "title0",
                    urls = listOf(),
                    description = "",
                    shootingDateStart = LocalDate.now(),
                    shootingDateEnd = LocalDate.now(),
                    visible = false,
                ),
                Video(
                    id = created1.id,
                    title = "title1",
                    urls = listOf(),
                    description = "",
                    shootingDateStart = LocalDate.now(),
                    shootingDateEnd = LocalDate.now(),
                    visible = false,
                ),
            )
        }
        assertSoftly(actual2) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!!.content.shouldContainExactly(
                Video(
                    id = created2.id,
                    title = "title2",
                    urls = listOf(),
                    description = "",
                    shootingDateStart = LocalDate.now(),
                    shootingDateEnd = LocalDate.now(),
                    visible = false,
                ),
                Video(
                    id = created3.id,
                    title = "title3",
                    urls = listOf(),
                    description = "",
                    shootingDateStart = LocalDate.now(),
                    shootingDateEnd = LocalDate.now(),
                    visible = false,
                ),
            )
        }
        client.deleteVideo(created0.id)
        client.deleteVideo(created1.id)
        client.deleteVideo(created2.id)
        client.deleteVideo(created3.id)
    }
}
