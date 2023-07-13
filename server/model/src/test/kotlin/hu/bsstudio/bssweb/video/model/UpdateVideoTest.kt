package hu.bsstudio.bssweb.video.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

@JsonTest
@ContextConfiguration(classes = [UpdateVideoTest::class])
class UpdateVideoTest(
    @Autowired private val underTest: JacksonTester<UpdateVideo>
) {

    @Test
    fun `test serialisation`() {
        val actual = this.underTest.write(UPDATE_VIDEO)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(UPDATE_VIDEO)
    }

    companion object {
        private const val url = "video_url"
        private const val title = "video_title"
        private const val description = "video_description"
        private val uploadedAt = LocalDate.now()
        private const val visible = true
        private val UPDATE_VIDEO = UpdateVideo(
            url = url,
            title = title,
            description = description,
            uploadedAt = uploadedAt,
            visible = visible
        )
        private val JSON = """
            {
                "url": "$url",
                "title": "$title",
                "description": "$description",
                "uploadedAt": "$uploadedAt",
                "visible": $visible
            }
        """.trimIndent()
    }
}
