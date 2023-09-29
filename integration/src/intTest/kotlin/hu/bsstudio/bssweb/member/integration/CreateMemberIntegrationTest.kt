package hu.bsstudio.bssweb.member.integration

import com.fasterxml.jackson.databind.ObjectMapper
import feign.FeignException
import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import java.net.URI
import java.time.LocalDate

class CreateMemberIntegrationTest(
    @Autowired private val client: MemberClient,
    @Value("\${bss.client.url}") private val url: String,
    @Autowired private val objectMapper: ObjectMapper
) : IntegrationTest() {

    @Test
    fun `it should return 201 and member body`() {
        val actual = client.createMember(CREATE_MEMBER)

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(201))
        assertThat(actual.body)
            .isNotNull
            .usingRecursiveAssertion()
            .ignoringFields("id")
            .isEqualTo(
                Member(
                    id = actual.body!!.id,
                    url = CREATE_MEMBER.url,
                    name = CREATE_MEMBER.name,
                    nickname = "",
                    description = "",
                    joinedAt = LocalDate.now(),
                    role = "",
                    status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
                    archived = false
                )
            )
        assertThat(actual.headers.location).isEqualTo(URI.create("$url/api/v1/member/${actual.body!!.id}"))
    }

    @Test
    fun `it should return 500 when duplicate urls were specified`() {
        memberRepository.save(DetailedMemberEntity(url = CREATE_MEMBER.url, name = CREATE_MEMBER.name))

        assertThatExceptionOfType(FeignException.InternalServerError::class.java)
            .isThrownBy { client.createMember(CreateMember(url = CREATE_MEMBER.url, name = CREATE_MEMBER.name)) }
            .satisfies({ assertThat(it.contentUTF8()).contains(""","status":500,"error":"Internal Server Error","path":"/api/v1/member"}""") })
    }

    private companion object {
        private val CREATE_MEMBER = CreateMember(url = "bcsik", name = "Bence Csik")
    }
}
