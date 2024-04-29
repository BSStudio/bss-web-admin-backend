package hu.bsstudio.bssweb.fileserver.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.util.UUID

@JsonTest
class MemberFileUpdateTest(
    @Autowired private val underTest: JacksonTester<MemberFileUpdate>,
) {
    @Test
    internal fun `test serialization`() {
        val actual = this.underTest.write(MEMBER_FILE_UPDATE)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual MEMBER_FILE_UPDATE
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val URL = "url"
        private val MEMBER_FILE_UPDATE =
            MemberFileUpdate(
                id = ID,
                url = URL,
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "url": "$URL"
            }
            """.trimIndent()
    }
}
