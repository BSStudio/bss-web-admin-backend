package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.member.common.MemberStatus
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.time.LocalDate
import java.util.UUID

@JsonTest
internal class MemberTest(
    @Autowired private val underTest: JacksonTester<Member>,
) {
    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(MEMBER)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual MEMBER
    }

    private companion object {
        private val ID = UUID.randomUUID()
        private const val URL = "member_url"
        private const val NAME = "member_name"
        private const val NICKNAME = "member_nickname"
        private const val DESCRIPTION = "member_description"
        private val JOINED_AT = LocalDate.now()
        private const val ROLE = "member_role"
        private val STATUS = MemberStatus.MEMBER_CANDIDATE_CANDIDATE
        private const val ARCHIVED = false
        private val MEMBER =
            Member(
                id = ID,
                url = URL,
                name = NAME,
                nickname = NICKNAME,
                description = DESCRIPTION,
                joinedAt = JOINED_AT,
                role = ROLE,
                status = STATUS,
                archived = ARCHIVED,
            )
        private val JSON =
            """
            {
                "id": "$ID",
                "url": "$URL",
                "name": "$NAME",
                "nickname": "$NICKNAME",
                "description": "$DESCRIPTION",
                "joinedAt": "$JOINED_AT",
                "role": "$ROLE",
                "status": "${STATUS.name}",
                "archived": $ARCHIVED
            }
            """.trimIndent()
    }
}
