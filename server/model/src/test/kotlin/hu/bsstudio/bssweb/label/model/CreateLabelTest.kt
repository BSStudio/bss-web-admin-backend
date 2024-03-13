package hu.bsstudio.bssweb.label.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
internal class CreateLabelTest(
    @Autowired private val underTest: JacksonTester<CreateLabel>,
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

    private companion object {
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private val CREATE_LABEL =
            CreateLabel(
                name = NAME,
                description = DESCRIPTION,
            )
        private val JSON =
            """
            {
                "name": "$NAME",
                "description": "$DESCRIPTION"
            }
            """.trimIndent()
    }
}
