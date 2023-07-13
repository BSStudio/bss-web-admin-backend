package hu.bsstudio.bssweb.videocrew.model

import hu.bsstudio.bssweb.member.model.SimpleMember
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [VideoCrewTest::class])
class VideoCrewTest(
    @Autowired private val underTest: JacksonTester<VideoCrew>
) {

    @Test
    fun `test serialisation`() {
        val actual = this.underTest.write(VIDEO_CREW)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(VIDEO_CREW)
    }

    companion object {
        private val videoId = UUID.randomUUID()
        private const val position = "Director"
        private val member = SimpleMember(
            id = UUID.randomUUID(),
            name = "Bence Csik",
            nickname = "CséBé"
        )
        private val VIDEO_CREW = VideoCrew(
            videoId = videoId,
            position = position,
            member = member
        )
        private val JSON = """
            {
                "videoId": "$videoId",
                "position": "$position",
                "member": {
                    "id": "${member.id}",
                    "name": "${member.name}",
                    "nickname": "${member.nickname}"
                }
            }
        """.trimIndent()
    }
}
