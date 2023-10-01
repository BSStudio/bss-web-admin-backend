package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.BssModelConfig
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [BssModelConfig::class])
internal class SimpleMemberTest(
    @Autowired private val underTest: JacksonTester<SimpleMember>
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
        private const val name = "John Doe"
        private const val nickname = "JD"
        private val SIMPLE_MEMBER = SimpleMember(
            id = ID,
            name = name,
            nickname = nickname
        )
        private val JSON = """
            {
                "id": "$ID",
                "name": "$name",
                "nickname": "$nickname"
            }
        """.trimIndent()
    }
}
