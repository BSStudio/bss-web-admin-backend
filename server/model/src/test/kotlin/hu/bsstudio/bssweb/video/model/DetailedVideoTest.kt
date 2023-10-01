package hu.bsstudio.bssweb.video.model

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
@ContextConfiguration(classes = [DetailedVideoTest::class])
internal class DetailedVideoTest(
    @Autowired private val underTest: JacksonTester<DetailedVideo>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(DETAILED_VIDEO)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual DETAILED_VIDEO
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val url = "video_url"
        private const val title = "video_title"
        private const val description = "video_description"
        private val uploadedAt = LocalDate.now()
        private const val visible = true
        private val DETAILED_VIDEO = DetailedVideo(
            id = ID,
            url = url,
            title = title,
            description = description,
            uploadedAt = uploadedAt,
            visible = visible,
            crew = listOf()
        )
        private val JSON = """
            {
                "id": "$ID",
                "url": "$url",
                "title": "$title",
                "description": "$description",
                "uploadedAt": "$uploadedAt",
                "visible": $visible,
                "crew": []
            }
        """.trimIndent()
    }
}
