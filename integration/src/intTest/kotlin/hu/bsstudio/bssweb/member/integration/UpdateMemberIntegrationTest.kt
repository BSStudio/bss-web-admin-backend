package hu.bsstudio.bssweb.member.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.time.LocalDate
import java.util.UUID

class UpdateMemberIntegrationTest(
    @Autowired private val client: MemberClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and updated member`() {
        val entity = this.memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.updateMember(entity.id, UPDATE_MEMBER)

        assertSoftly(actual) {
            body!! shouldBeEqual Member(
                id = entity.id,
                url = UPDATE_MEMBER.url,
                name = UPDATE_MEMBER.name,
                nickname = UPDATE_MEMBER.nickname,
                description = UPDATE_MEMBER.description,
                joinedAt = UPDATE_MEMBER.joinedAt,
                role = UPDATE_MEMBER.role,
                status = UPDATE_MEMBER.status,
                archived = UPDATE_MEMBER.archived
            )
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        }
    }

    @Test
    fun `it should return 404 when member not found`() {
        shouldThrow<FeignException.NotFound> {
            client.updateMember(UUID.fromString("00000000-0000-0000-0000-000000000000"), UPDATE_MEMBER)
        }
    }

    private companion object {
        private val UPDATE_MEMBER = UpdateMember(
            url = "updatedUrl",
            name = "updatedName",
            nickname = "updatedNickname",
            description = "updatedDescription",
            joinedAt = LocalDate.of(2023, 1, 1),
            role = "updatedRole",
            status = MemberStatus.ALUMNI,
            archived = true
        )
    }
}
