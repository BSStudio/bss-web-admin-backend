package hu.bsstudio.bssweb.member.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.model.Member
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

class ReadMemberIntegrationTest(
    @Autowired private val client: MemberClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.getMemberById(entity.id)

        assertThat(actual.body).isEqualTo(
            Member(
                id = entity.id,
                url = entity.url,
                name = entity.name,
                nickname = entity.nickname,
                description = entity.description,
                joinedAt = entity.joinedAt,
                role = entity.role,
                status = entity.status,
                archived = entity.archived
            )
        )
        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
    }

    @Test
    fun `it should return 404 when member not found`() {
        Assertions.assertThatExceptionOfType(FeignException.NotFound::class.java)
            .isThrownBy { client.getMemberById(UUID.fromString("00000000-0000-0000-0000-000000000000")) }
    }
}
