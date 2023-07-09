package hu.bsstudio.bssweb.videocrew.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [VideoCrewRequestTest::class])
class VideoCrewRequestTest(
    @Autowired private val underTest: JacksonTester<VideoCrewRequest>
) {

    @Test
    fun `test serialisation`() {
        val actual = this.underTest.write(VIDEO_CREW_REQUEST)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(VIDEO_CREW_REQUEST)
    }

    companion object {
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
