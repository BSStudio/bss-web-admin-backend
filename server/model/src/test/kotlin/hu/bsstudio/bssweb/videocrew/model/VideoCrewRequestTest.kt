package hu.bsstudio.bssweb.videocrew.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.util.UUID

@JsonTest
internal class VideoCrewRequestTest(
    @Autowired private val underTest: JacksonTester<VideoCrewRequest>,
) {
    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(VIDEO_CREW_REQUEST)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual VIDEO_CREW_REQUEST
    }

    private companion object {
        private val VIDEO_ID = UUID.randomUUID()
        private const val POSITION = "Director"
        private val MEMBER_ID = UUID.randomUUID()
        private val VIDEO_CREW_REQUEST =
            VideoCrewRequest(
                videoId = VIDEO_ID,
                position = POSITION,
                memberId = MEMBER_ID,
            )
        private val JSON =
            """
            {
                "videoId": "$VIDEO_ID",
                "position": "$POSITION",
                "memberId": "$MEMBER_ID"
            }
            """.trimIndent()
    }
}
