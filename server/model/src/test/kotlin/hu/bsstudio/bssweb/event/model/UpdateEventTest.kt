package hu.bsstudio.bssweb.event.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.time.LocalDate

@JsonTest
internal class UpdateEventTest(
    @Autowired private val underTest: JacksonTester<UpdateEvent>,
) {
    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(UPDATE_EVENT)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual UPDATE_EVENT
    }

    private companion object {
        private const val URL = "url"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private val DATE = LocalDate.now()
        private const val VISIBLE = true
        private val UPDATE_EVENT =
            UpdateEvent(
                url = URL,
                title = TITLE,
                description = DESCRIPTION,
                date = DATE,
                visible = VISIBLE,
            )
        private val JSON =
            """
            {
                "url": "$URL",
                "title": "$TITLE",
                "description": "$DESCRIPTION",
                "date": "$DATE",
                "visible": $VISIBLE
            }
            """.trimIndent()
    }
}
