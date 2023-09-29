package hu.bsstudio.bssweb.member.integration

import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.*

class ArchiveMemberIntegrationTest(
    @Autowired private val client: MemberClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and archive member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.archiveMembers(listOf(entity.id), true)

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body).isEqualTo(listOf(entity.id))
    }

    @Test
    fun `it should return 200 and unarchive member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name", archived = true))

        val actual = client.archiveMembers(listOf(entity.id), false)

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body).isEqualTo(listOf(entity.id))
    }

    @Test
    fun `it should return 200 and empty list when id can't be found`() {
        val actual = client.archiveMembers(listOf(UUID.fromString("00000000-0000-0000-0000-000000000000")), true)

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body).isEmpty()
    }
}
