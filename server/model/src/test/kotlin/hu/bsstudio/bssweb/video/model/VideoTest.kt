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
internal class VideoTest(
    @Autowired private val underTest: JacksonTester<Video>,
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
        private const val TITLE = "video_title"
        private const val DESCRIPTION = "description"
        private val URLS = listOf("video_url")
        private val SHOOTING_DATE_START = LocalDate.EPOCH
        private val SHOOTING_DATE_END = LocalDate.MAX
        private const val VISIBLE = true
        private val VIDEO =
            Video(
                id = ID,
                title = TITLE,
                urls = URLS,
                description = DESCRIPTION,
                shootingDateStart = SHOOTING_DATE_START,
                shootingDateEnd = SHOOTING_DATE_END,
                visible = VISIBLE,
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "title": "$TITLE",
                "description": "$DESCRIPTION",
                "urls": ["${URLS[0]}"],
                "shootingDateStart": "$SHOOTING_DATE_START",
                "shootingDateEnd": "$SHOOTING_DATE_END",
                "visible": $VISIBLE
            }
            """.trimIndent()
    }
}
