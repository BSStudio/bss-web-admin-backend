package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

class DeleteMemberIntegrationTest(
    @Autowired private val client: MemberClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 204 and delete member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.removeMember(entity.id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }

    @Test
    internal fun `it should return 204 when member not found`() {
        val actual = client.removeMember(UUID.fromString("00000000-0000-0000-0000-000000000000"))

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }
}
