package hu.bsstudio.bssweb.videocrew.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [VideoCrewRequestTest::class])
internal class VideoCrewRequestTest(
    @Autowired private val underTest: JacksonTester<VideoCrewRequest>
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
        private val videoId = UUID.randomUUID()
        private const val position = "Director"
        private val memberId = UUID.randomUUID()
        private val VIDEO_CREW_REQUEST = VideoCrewRequest(
            videoId = videoId,
            position = position,
            memberId = memberId
        )
        private val JSON = """
            {
                "videoId": "$videoId",
                "position": "$position",
                "memberId": "$memberId"
            }
        """.trimIndent()
    }
}
