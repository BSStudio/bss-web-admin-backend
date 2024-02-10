package hu.bsstudio.bssweb.videocrew.model

import hu.bsstudio.bssweb.member.model.SimpleMember
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.util.UUID

@JsonTest
internal class VideoCrewTest(
    @Autowired private val underTest: JacksonTester<VideoCrew>,
) {
    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(VIDEO_CREW)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual VIDEO_CREW
    }

    private companion object {
        private val VIDEO_ID = UUID.randomUUID()
        private const val POSITION = "Director"
        private val MEMBER =
            SimpleMember(
                id = UUID.randomUUID(),
                name = "Bence Csik",
                nickname = "CséBé",
            )
        private val VIDEO_CREW =
            VideoCrew(
                videoId = VIDEO_ID,
                position = POSITION,
                member = MEMBER,
            )
        private val JSON =
            """
            {
                "videoId": "$VIDEO_ID",
                "position": "$POSITION",
                "member": {
                    "id": "${MEMBER.id}",
                    "name": "${MEMBER.name}",
                    "nickname": "${MEMBER.nickname}"
                }
            }
            """.trimIndent()
    }
}
