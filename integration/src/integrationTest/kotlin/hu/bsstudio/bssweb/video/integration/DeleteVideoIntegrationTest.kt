package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.entity.VideoEntity
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBeEmpty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatusCode
import java.util.UUID

class DeleteVideoIntegrationTest(
    @param:Autowired private val client: VideoClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 204 and soft delete video`() {
        val entity = videoRepository.save(VideoEntity(urls = emptyList(), title = "title"))

        val actual = client.deleteVideo(entity.id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
        videoRepository.findById(entity.id).shouldBeEmpty()
        videoRepository.findAllDeleted(Pageable.unpaged()).content.map { it.id } shouldContainExactly listOf(entity.id)
    }

    @Test
    internal fun `it should return 204 when video not found`() {
        val actual = client.deleteVideo(UUID.fromString("00000000-0000-0000-0000-000000000000"))

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }
}
