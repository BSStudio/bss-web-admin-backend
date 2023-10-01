package hu.bsstudio.bssweb.event.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

@JsonTest
@ContextConfiguration(classes = [UpdateEventTest::class])
internal class UpdateEventTest(
    @Autowired private val underTest: JacksonTester<UpdateEvent>
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
        private const val url = "url"
        private const val title = "title"
        private const val description = "description"
        private val date = LocalDate.now()
        private const val visible = true
        private val UPDATE_EVENT = UpdateEvent(
            url = url,
            title = title,
            description = description,
            date = date,
            visible = visible
        )
        private val JSON = """
            {
                "url": "$url",
                "title": "$title",
                "description": "$description",
                "date": "$date",
                "visible": $visible
            }
        """.trimIndent()
    }
}
