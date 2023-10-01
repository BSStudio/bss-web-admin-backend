package hu.bsstudio.bssweb.event.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [EventTest::class])
internal class EventTest(
    @Autowired private val underTest: JacksonTester<Event>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(EVENT)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual EVENT
    }

    private companion object {
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
