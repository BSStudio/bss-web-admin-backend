package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTestBase
import hu.bsstudio.bssweb.client.BssWebClient
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreateMemberTest(
    @Autowired private val client: BssWebClient,
    @Autowired private val memberRepository: MemberRepository
): IntegrationTestBase() {

    @Test
    fun testName() {
        val actual = client.createMember(CreateMember(url = "bcsik", name = "Bence Csik"))
        // assertThat(actual.body).isEqualTo(Member())
        // assertThat(actual.statusCode).isEqualTo()
        // memberRepository.deleteAll()
    }

    @Test
    fun testName2() {
        val actual1 = client.createMember(CreateMember(url = "bcsik", name = "Bence Csik"))
        val actual2 = client.createMember(CreateMember(url = "bcsik", name = "Bence Csik"))
        // assertThat(actual.body).isEqualTo(Member())
        // assertThat(actual.statusCode).isEqualTo()
        // memberRepository.deleteAll()
    }
}
