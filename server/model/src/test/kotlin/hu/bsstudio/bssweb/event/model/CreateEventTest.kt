package hu.bsstudio.bssweb.event.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.json.JsonContent
import org.springframework.test.context.ContextConfiguration

@JsonTest
@ContextConfiguration(classes = [CreateEventTest::class])
class CreateEventTest(
    @Autowired private val underTest: JacksonTester<CreateEvent>
) {

    @Test
    fun `test serialisation`() {
        val actual: JsonContent<CreateEvent> = this.underTest.write(CREATE_EVENT)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    fun `test deserialization`() {
        val actual: CreateEvent = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(CREATE_EVENT)
    }

    companion object {
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
