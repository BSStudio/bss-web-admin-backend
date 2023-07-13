package hu.bsstudio.bssweb.event.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [DetailedEventTest::class])
internal class DetailedEventTest(
    @Autowired private val underTest: JacksonTester<DetailedEvent>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(DETAILED_EVENT)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(DETAILED_EVENT)
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val url = "url"
        private const val title = "title"
        private const val description = "description"
        private val date = LocalDate.now()
        private const val visible = true
        private val DETAILED_EVENT = DetailedEvent(
            id = ID,
            url = url,
            title = title,
            description = description,
            date = date,
            visible = visible,
            videos = listOf()
        )
        private val JSON = """
            {
                "id": "$ID",
                "url": "$url",
                "title": "$title",
                "description": "$description",
                "date": "$date",
                "visible": $visible,
                "videos": []
            }
        """.trimIndent()
    }
}
