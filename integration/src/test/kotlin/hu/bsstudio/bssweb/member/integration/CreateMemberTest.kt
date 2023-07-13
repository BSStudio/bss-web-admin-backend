package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTestConfig
import hu.bsstudio.bssweb.client.BssWebClient
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatusCode
import java.net.URI
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [IntegrationTestConfig::class])
class CreateMemberTest(
    @Autowired private val client: BssWebClient,
    @Autowired private val memberRepository: MemberRepository
) {

    @Test
    fun testName() {
        val actual = client.createMember(CreateMember(url = "bcsik", name = "Bence Csik"))

        assertThat(actual.body)
                .isNotNull
                .usingRecursiveAssertion()
                .ignoringFields("id")
                .isEqualTo(Member(
                        id = actual.body!!.id,
                        url = "bcsik",
                        name = "Bence Csik",
                        nickname = "",
                        description = "",
                        joinedAt = LocalDate.now(),
                        role = "",
                        status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
                        archived = false
                ))
        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(201))
        assertThat(actual.headers.location).isEqualTo(URI.create("http://localhost:8080/api/members/${actual.body!!.id}}"))
        // memberRepository.deleteAll()
    }

    @Test
    fun testName2() {
        client.createMember(CreateMember(url = "bcsik", name = "Bence Csik"))
        client.createMember(CreateMember(url = "bcsik", name = "Bence Csik"))
        // assertThat(actual.body).isEqualTo(Member())
        // assertThat(actual.statusCode).isEqualTo()
        // memberRepository.deleteAll()
    }
}
