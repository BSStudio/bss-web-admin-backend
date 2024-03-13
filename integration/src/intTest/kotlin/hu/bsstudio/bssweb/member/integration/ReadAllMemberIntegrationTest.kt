package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.model.Member
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode

class ReadAllMemberIntegrationTest(
    @Autowired private val client: MemberClient
) : IntegrationTest() {

    @Test
    internal fun `it should return 200 and empty list`() {
        val actual = client.getAllMembers()

        assertSoftly(actual) {
            body.shouldBeEmpty()
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        }
    }

    @Test
    internal fun `it should return 200 and list with 1 member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.getAllMembers()

        assertSoftly(actual) {
            body.shouldContainExactly(
                Member(
                    id = entity.id,
                    url = entity.url,
                    name = entity.name,
                    nickname = entity.nickname,
                    joinedAt = entity.joinedAt,
                    description = entity.description,
                    role = entity.role,
                    status = entity.status,
                    archived = entity.archived
                )
            )
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        }
    }
}
