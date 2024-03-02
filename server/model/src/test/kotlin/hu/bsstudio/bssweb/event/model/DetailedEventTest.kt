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
internal class DetailedEventTest(
    @Autowired private val underTest: JacksonTester<DetailedEvent>,
) {
    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(DETAILED_EVENT)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual DETAILED_EVENT
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val URL = "url"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private val DATE = LocalDate.now()
        private const val VISIBLE = true
        private val DETAILED_EVENT =
            DetailedEvent(
                id = ID,
                url = URL,
                title = TITLE,
                description = DESCRIPTION,
                dateFrom = DATE,
                visible = VISIBLE,
                videos = listOf(),
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "url": "$URL",
                "title": "$TITLE",
                "description": "$DESCRIPTION",
                "date": "$DATE",
                "visible": $VISIBLE,
                "videos": []
            }
            """.trimIndent()
    }
}
