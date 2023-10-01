package hu.bsstudio.bssweb.video.model

import hu.bsstudio.bssweb.BssModelConfig
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [BssModelConfig::class])
internal class VideoTest(
    @Autowired private val underTest: JacksonTester<Video>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(VIDEO)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual VIDEO
    }

    private companion object {
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
