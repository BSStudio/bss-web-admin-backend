package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.BssModelConfig
import hu.bsstudio.bssweb.member.common.MemberStatus
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

@JsonTest
@ContextConfiguration(classes = [BssModelConfig::class])
internal class UpdateMemberTest(
    @Autowired private val underTest: JacksonTester<UpdateMember>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(UPDATE_MEMBER)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual UPDATE_MEMBER
    }

    private companion object {
        private const val url = "member_url"
        private const val name = "member_name"
        private const val nickname = "member_nickname"
        private const val description = "member_description"
        private val joinedAt = LocalDate.now()
        private const val role = "member_role"
        private val status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE
        private const val archived = false
        private val UPDATE_MEMBER = UpdateMember(
            url = url,
            name = name,
            nickname = nickname,
            description = description,
            joinedAt = joinedAt,
            role = role,
            status = status,
            archived = archived
        )
        private val JSON = """
            {
                "url": "$url",
                "name": "$name",
                "nickname": "$nickname",
                "description": "$description",
                "joinedAt": "$joinedAt",
                "role": "$role",
                "status": "${status.name}",
                "archived": $archived
            }
        """.trimIndent()
    }
}
