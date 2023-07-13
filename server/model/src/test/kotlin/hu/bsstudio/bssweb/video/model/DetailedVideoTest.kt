package hu.bsstudio.bssweb.video.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [DetailedVideoTest::class])
class DetailedVideoTest(
    @Autowired private val underTest: JacksonTester<DetailedVideo>
) {

    @Test
    fun `test serialisation`() {
        val actual = this.underTest.write(DETAILED_VIDEO)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(DETAILED_VIDEO)
    }

    companion object {
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
