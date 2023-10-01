package hu.bsstudio.bssweb.event.model

import hu.bsstudio.bssweb.BssModelConfig
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
@ContextConfiguration(classes = [BssModelConfig::class])
internal class DetailedEventTest(
    @Autowired private val underTest: JacksonTester<DetailedEvent>
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
