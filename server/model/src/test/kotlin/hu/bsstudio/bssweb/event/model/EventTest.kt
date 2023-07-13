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
@ContextConfiguration(classes = [EventTest::class])
class EventTest(
    @Autowired private val underTest: JacksonTester<Event>
) {

    @Test
    fun `test serialisation`() {
        val actual = this.underTest.write(EVENT)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(EVENT)
    }

    companion object {
        private val ID = UUID.randomUUID()
        private const val url = "url"
        private const val title = "title"
        private const val description = "description"
        private val date = LocalDate.now()
        private const val visible = true
        private val EVENT = Event(
            id = ID,
            url = url,
            title = title,
            description = description,
            date = date,
            visible = visible
        )
        private val JSON = """
            {
                "id": "$ID",
                "url": "$url",
                "title": "$title",
                "description": "$description",
                "date": "$date",
                "visible": $visible
            }
        """.trimIndent()
    }
}
