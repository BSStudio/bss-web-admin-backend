package hu.bsstudio.bssweb.member.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration

@JsonTest
@ContextConfiguration(classes = [CreateMemberTest::class])
internal class CreateMemberTest(
    @Autowired private val underTest: JacksonTester<CreateMember>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(CREATE_MEMBER)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(CREATE_MEMBER)
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
