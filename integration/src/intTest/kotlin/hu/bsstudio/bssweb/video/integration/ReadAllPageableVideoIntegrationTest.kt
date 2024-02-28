package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.Video
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatusCode
import java.time.LocalDate
import java.util.UUID

class ReadAllPageableVideoIntegrationTest(
    @Autowired private val client: VideoClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and paged`() {
        val (entity0, entity1, entity2, entity3) = videoRepository.saveAll(
            IntRange(0, 3).map { i ->
                DetailedVideoEntity(urls = listOf("url$i"), title = "title$i").apply {
                    id = UUID.fromString("00000000-0000-0000-0000-00000000000$i")
                }
            }
        )

        val actual1 = client.getAllVideos(PageRequest.of(0, 2))
        val actual2 = client.getAllVideos(PageRequest.of(1, 2))

        assertSoftly(actual1) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!!.content.shouldContainExactly(
                Video(
                    id = entity0.id,
                    urls = listOf("url0"),
                    title = "title0",
                    uploadedAt = LocalDate.now(),
                    visible = false
                ),
                Video(
                    id = entity1.id,
                    urls = listOf("url1"),
                    title = "title1",
                    uploadedAt = LocalDate.now(),
                    visible = false
                )
            )
        }
        assertSoftly(actual2) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!!.content.shouldContainExactly(
                Video(
                    id = entity2.id,
                    urls = listOf("url2"),
                    title = "title2",
                    uploadedAt = LocalDate.now(),
                    visible = false
                ),
                Video(
                    id = entity3.id,
                    urls = listOf("url3"),
                    title = "title3",
                    uploadedAt = LocalDate.now(),
                    visible = false
                )
            )
        }
    }
}
