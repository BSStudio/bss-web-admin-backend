package hu.bsstudio.bssweb.revision.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JsonTest
internal class RevisionInfoTest(
    @param:Autowired private val underTest: JacksonTester<RevisionInfo>,
) {
    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(REVISION_INFO)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual REVISION_INFO
    }

    private companion object {
        private const val NUMBER = 42L
        private val TIMESTAMP = Instant.parse("2026-10-30T12:00:00Z")
        private const val AUTHOR = "author"
        private val TYPE = RevisionType.RESTORED
        private val REVISION_INFO = RevisionInfo(number = NUMBER, timestamp = TIMESTAMP, author = AUTHOR, type = TYPE)
        private val JSON =
            """
            {
                "number": $NUMBER,
                "timestamp": "$TIMESTAMP",
                "author": "$AUTHOR",
                "type": "$TYPE"
            }
            """.trimIndent()
    }
}
