package hu.bsstudio.bssweb.video.model

import hu.bsstudio.bssweb.BssModelConfig
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration

@JsonTest
@ContextConfiguration(classes = [BssModelConfig::class])
internal class CreateVideoTest(
    @Autowired private val underTest: JacksonTester<CreateVideo>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(CREATE_VIDEO)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual CREATE_VIDEO
    }

    private companion object {
        private const val url = "video_url"
        private const val title = "video_title"
        private val CREATE_VIDEO = CreateVideo(
            url = url,
            title = title
        )
        private val JSON = """
            {
                "url": "$url",
                "title": "$title"
            }
        """.trimIndent()
    }
}
