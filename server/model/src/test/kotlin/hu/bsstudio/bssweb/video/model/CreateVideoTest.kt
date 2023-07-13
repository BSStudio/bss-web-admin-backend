package hu.bsstudio.bssweb.video.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration

@JsonTest
@ContextConfiguration(classes = [CreateVideoTest::class])
internal class CreateVideoTest(
    @Autowired private val underTest: JacksonTester<CreateVideo>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(CREATE_VIDEO)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(CREATE_VIDEO)
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
