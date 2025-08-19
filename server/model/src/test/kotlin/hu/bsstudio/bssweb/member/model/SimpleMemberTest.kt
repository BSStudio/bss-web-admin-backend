package hu.bsstudio.bssweb.member.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.util.UUID

@JsonTest
internal class SimpleMemberTest(
    @param:Autowired private val underTest: JacksonTester<SimpleMember>,
) {
    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(SIMPLE_MEMBER)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual SIMPLE_MEMBER
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val NAME = "John Doe"
        private const val NICKNAME = "JD"
        private val SIMPLE_MEMBER =
            SimpleMember(
                id = ID,
                name = NAME,
                nickname = NICKNAME,
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "name": "$NAME",
                "nickname": "$NICKNAME"
            }
            """.trimIndent()
    }
}
