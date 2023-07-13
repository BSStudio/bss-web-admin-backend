package hu.bsstudio.bssweb.member.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [SimpleMemberTest::class])
internal class SimpleMemberTest(
    @Autowired private val underTest: JacksonTester<SimpleMember>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(SIMPLE_MEMBER)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(SIMPLE_MEMBER)
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
