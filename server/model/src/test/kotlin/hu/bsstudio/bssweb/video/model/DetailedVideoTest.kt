package hu.bsstudio.bssweb.video.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.time.LocalDate
import java.util.UUID

@JsonTest
internal class DetailedVideoTest(
    @Autowired private val underTest: JacksonTester<DetailedVideo>,
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
        private const val URL = "video_url"
        private const val TITLE = "video_title"
        private const val DESCRIPTION = "video_description"
        private val UPLOADED_AT = LocalDate.now()
        private const val VISIBLE = true
        private val DETAILED_VIDEO =
            DetailedVideo(
                id = ID,
                url = URL,
                title = TITLE,
                description = DESCRIPTION,
                uploadedAt = UPLOADED_AT,
                visible = VISIBLE,
                crew = listOf(),
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "url": "$URL",
                "title": "$TITLE",
                "description": "$DESCRIPTION",
                "uploadedAt": "$UPLOADED_AT",
                "visible": $VISIBLE,
                "crew": []
            }
            """.trimIndent()
    }
}
