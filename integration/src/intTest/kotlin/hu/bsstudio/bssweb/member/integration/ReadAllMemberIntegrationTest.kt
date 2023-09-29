package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode

class ReadAllMemberIntegrationTest(
    @Autowired private val client: MemberClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and empty list`() {
        val actual = client.getAllMembers()

        assertThat(actual.body).isEmpty()
        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
    }

    @Test
    fun `it should return 200 and list with 1 member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.getAllMembers()

        assertThat(actual.body).containsExactly(
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
        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
    }
}
