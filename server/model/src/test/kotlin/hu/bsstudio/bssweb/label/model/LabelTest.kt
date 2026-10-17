package hu.bsstudio.bssweb.label.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.util.UUID

@JsonTest
internal class LabelTest(
    @param:Autowired private val underTest: JacksonTester<Label>,
) {
    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(CREATE_LABEL)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual CREATE_LABEL
    }

    @Test
    internal fun `test deserialization without a video count`() {
        val actual = this.underTest.parseObject(JSON.replace(Regex(",\\s*\"videoCount\": \\d+"), ""))

        actual shouldBeEqual CREATE_LABEL.copy(videoCount = 0)
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private val CREATE_LABEL =
            Label(
                id = ID,
                name = NAME,
                description = DESCRIPTION,
                videoCount = 2,
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "name": "$NAME",
                "description": "$DESCRIPTION",
                "videoCount": 2
            }
            """.trimIndent()
    }
}
