package hu.bsstudio.bssweb.fileserver.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.util.UUID

@JsonTest
class VideoFileUpdateTest(
    @Autowired private val underTest: JacksonTester<VideoFileUpdate>,
) {
    @Test
    internal fun `test serialization`() {
        val actual = this.underTest.write(VIDEO_FILE_UPDATE)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual VIDEO_FILE_UPDATE
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private val URLS = listOf("url")
        private val VIDEO_FILE_UPDATE =
            VideoFileUpdate(
                id = ID,
                urls = URLS,
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "urls": ["${URLS.get(0)}"]
            }
            """.trimIndent()
    }
}
