package hu.bsstudio.bssweb.event.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.time.LocalDate
import java.util.UUID

@JsonTest
internal class EventTest(
    @Autowired private val underTest: JacksonTester<Event>,
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
        private const val URL = "url"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private val DATE_FROM = LocalDate.EPOCH
        private val DATE_TO = LocalDate.MAX
        private const val VISIBLE = true
        private val EVENT =
            Event(
                id = ID,
                url = URL,
                title = TITLE,
                description = DESCRIPTION,
                dateFrom = DATE_FROM,
                dateTo = DATE_TO,
                visible = VISIBLE,
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "url": "$URL",
                "title": "$TITLE",
                "description": "$DESCRIPTION",
                "dateFrom": "$DATE_FROM",
                "dateTo": "$DATE_TO",
                "visible": $VISIBLE
            }
            """.trimIndent()
    }
}
