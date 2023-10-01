package hu.bsstudio.bssweb.video.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

@JsonTest
@ContextConfiguration(classes = [UpdateVideoTest::class])
internal class UpdateVideoTest(
    @Autowired private val underTest: JacksonTester<UpdateVideo>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(UPDATE_VIDEO)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual UPDATE_VIDEO
    }

    private companion object {
        private const val url = "video_url"
        private const val title = "video_title"
        private const val description = "video_description"
        private val uploadedAt = LocalDate.now()
        private const val visible = true
        private val UPDATE_VIDEO = UpdateVideo(
            url = url,
            title = title,
            description = description,
            uploadedAt = uploadedAt,
            visible = visible
        )
        private val JSON = """
            {
                "url": "$url",
                "title": "$title",
                "description": "$description",
                "uploadedAt": "$uploadedAt",
                "visible": $visible
            }
        """.trimIndent()
    }
}
