package hu.bsstudio.bssweb.video.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [VideoTest::class])
class VideoTest(
    @Autowired private val underTest: JacksonTester<Video>
) {

    @Test
    fun `test serialisation`() {
        val actual = this.underTest.write(VIDEO)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(VIDEO)
    }

    companion object {
        private val ID = UUID.randomUUID()
        private const val url = "video_url"
        private const val title = "video_title"
        private val uploadedAt = LocalDate.now()
        private const val visible = true
        private val VIDEO = Video(
            id = ID,
            url = url,
            title = title,
            uploadedAt = uploadedAt,
            visible = visible
        )
        private val JSON = """
            {
                "id": "$ID",
                "url": "$url",
                "title": "$title",
                "uploadedAt": "$uploadedAt",
                "visible": $visible
            }
        """.trimIndent()
    }
}
