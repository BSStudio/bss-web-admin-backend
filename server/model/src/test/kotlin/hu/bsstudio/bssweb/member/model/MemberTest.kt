package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.member.common.MemberStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.UUID

@JsonTest
@ContextConfiguration(classes = [MemberTest::class])
internal class MemberTest(
    @Autowired private val underTest: JacksonTester<Member>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(MEMBER)

        assertThat(actual).isEqualToJson(JSON)
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        assertThat(actual).isEqualTo(MEMBER)
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val url = "member_url"
        private const val name = "member_name"
        private const val nickname = "member_nickname"
        private const val description = "member_description"
        private val joinedAt = LocalDate.now()
        private const val role = "member_role"
        private val status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE
        private const val archived = false
        private val MEMBER = Member(
            id = ID,
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
                "id": "$ID",
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
