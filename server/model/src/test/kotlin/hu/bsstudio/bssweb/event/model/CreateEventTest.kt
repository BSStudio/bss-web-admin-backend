package hu.bsstudio.bssweb.event.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
internal class CreateEventTest(
    @Autowired private val underTest: JacksonTester<CreateEvent>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(CREATE_EVENT)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual CREATE_EVENT
    }

    private companion object {
        private const val URL = "url"
        private const val TITLE = "title"
        private val CREATE_EVENT = CreateEvent(url = URL, title = TITLE)
        private val JSON = """
            {
                "url": "$URL",
                "title": "$TITLE"
            }
        """.trimIndent()
    }
}
