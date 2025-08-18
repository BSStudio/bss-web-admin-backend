package hu.bsstudio.bssweb.member.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.model.Member
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

class ReadMemberIntegrationTest(
    @param:Autowired private val client: MemberClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.getMemberById(entity.id)

        assertSoftly(actual) {
            body!! shouldBeEqual
                Member(
                    id = entity.id,
                    url = entity.url,
                    name = entity.name,
                    nickname = entity.nickname,
                    description = entity.description,
                    joinedAt = entity.joinedAt,
                    role = entity.role,
                    status = entity.status,
                    archived = entity.archived,
                )
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        }
    }

    @Test
    internal fun `it should return 404 when member not found`() {
        shouldThrow<FeignException.NotFound> {
            client.getMemberById(UUID.fromString("00000000-0000-0000-0000-000000000000"))
        }
    }
}
