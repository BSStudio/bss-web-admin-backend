package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.entity.MemberEntity
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBeEmpty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatusCode
import java.util.UUID

class DeleteMemberIntegrationTest(
    @param:Autowired private val client: MemberClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 204 and soft delete member`() {
        val entity = memberRepository.save(MemberEntity(url = "url", name = "name"))

        val actual = client.removeMember(entity.id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
        memberRepository.findById(entity.id).shouldBeEmpty()
        memberRepository.findAllDeleted(Pageable.unpaged()).content.map { it.id } shouldContainExactly listOf(entity.id)
    }

    @Test
    internal fun `it should return 204 when member not found`() {
        val actual = client.removeMember(UUID.fromString("00000000-0000-0000-0000-000000000000"))

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }
}
