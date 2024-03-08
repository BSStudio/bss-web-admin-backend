package hu.bsstudio.bssweb.video.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.time.LocalDate

@JsonTest
internal class UpdateVideoTest(
    @Autowired private val underTest: JacksonTester<UpdateVideo>,
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
        private val URLS = listOf("video_url")
        private const val TITLE = "video_title"
        private const val DESCRIPTION = "video_description"
        private val SHOOTING_DATE_START = LocalDate.EPOCH
        private val SHOOTING_DATE_END = LocalDate.MAX
        private const val VISIBLE = true
        private val LABELS = listOf("category")
        private val UPDATE_VIDEO =
            UpdateVideo(
                urls = URLS,
                title = TITLE,
                description = DESCRIPTION,
                shootingDateStart = SHOOTING_DATE_START,
                shootingDateEnd = SHOOTING_DATE_END,
                visible = VISIBLE,
                labels = LABELS,
            )
        private val JSON =
            """
            {
                "urls": ["${URLS[0]}"],
                "title": "$TITLE",
                "description": "$DESCRIPTION",
                "shootingDateStart": "$SHOOTING_DATE_START",
                "shootingDateEnd": "$SHOOTING_DATE_END",
                "visible": $VISIBLE,
                "labels": ["${LABELS[0]}"]
            }
            """.trimIndent()
    }
}
