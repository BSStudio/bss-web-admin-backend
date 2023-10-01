package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.BssModelConfig
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration

@JsonTest
@ContextConfiguration(classes = [BssModelConfig::class])
internal class CreateMemberTest(
    @Autowired private val underTest: JacksonTester<CreateMember>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(CREATE_MEMBER)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual CREATE_MEMBER
    }

    private companion object {
        private const val url = "member_url"
        private const val name = "member_name"
        private val CREATE_MEMBER = CreateMember(
            url = url,
            name = name
        )
        private val JSON = """
            {
                "url": "$url",
                "name": "$name"
            }
        """.trimIndent()
    }
}
