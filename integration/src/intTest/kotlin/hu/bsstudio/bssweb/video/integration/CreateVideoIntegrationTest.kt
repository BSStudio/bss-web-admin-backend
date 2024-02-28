package hu.bsstudio.bssweb.video.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.Video
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDate

class CreateVideoIntegrationTest(
    @Autowired private val client: VideoClient,
    @Value("\${bss.client.url}") private val url: String
) : IntegrationTest() {

    @Test
    fun `it should return 201 and video`() {
        val actual = client.createVideo(CREATE_EVENT)

        assertSoftly(actual) {
            statusCode shouldBeEqual org.springframework.http.HttpStatusCode.valueOf(201)
            body!! shouldBeEqual Video(
                id = actual.body!!.id,
                urls = emptyList(),
                title = CREATE_EVENT.title,
                visible = false,
                uploadedAt = LocalDate.now()
            )
            headers.location!! shouldBeEqual java.net.URI.create("$url/api/v1/video/${actual.body!!.id}")
        }
    }

    @Test
    fun `it should retun 500 when duplicate urls were specified`() {
        videoRepository.save(DetailedVideoEntity(title = CREATE_EVENT.title))

        shouldThrow<FeignException.InternalServerError> {
            client.createVideo(CREATE_EVENT)
        } should { it.contentUTF8() shouldContain ""","status":500,"error":"Internal Server Error","path":"/api/v1/video"}""" }
    }

    private companion object {
        private val CREATE_EVENT = CreateVideo(
            title = "title"
        )
    }
}
