package hu.bsstudio.bssweb.member.integration

import feign.FeignException
import hu.bsstudio.bssweb.BssFeignConfig
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.net.URI
import java.nio.charset.Charset
import java.time.LocalDate
import kotlin.random.Random

@SpringJUnitConfig(classes = [BssFeignConfig::class])
@TestPropertySource(
    properties = [
        "spring.cloud.openfeign.client.config.member.url=http://localhost:9999",
    ]
)
class CreateMemberTest {

    @Autowired
    private lateinit var client: MemberClient

    @BeforeEach
    fun setUp() {
        client.getAllMembers().body?.forEach { client.removeMember(it.id) }
    }

    @Test
    fun `it should return 200 and member body`() {
        val actual = client.createMember(CreateMember(url = "bcsik", name = "Bence Csik"))

        assertThat(actual.body)
            .isNotNull
            .usingRecursiveAssertion()
            .ignoringFields("id")
            .isEqualTo(
                Member(
                    id = actual.body!!.id,
                    url = "bcsik",
                    name = "Bence Csik",
                    nickname = "",
                    description = "",
                    joinedAt = LocalDate.now(),
                    role = "",
                    status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
                    archived = false
                )
            )
        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(201))
        assertThat(actual.headers.location).isEqualTo(URI.create("http://localhost:9999/api/v1/member/${actual.body!!
            .id}"))
        actual.body?.let { client.removeMember(it.id) }
    }

    @Test
    fun `it should return 500 when duplicate urls were specified`() {
        val member = client.createMember(CreateMember(url = "bcsik", name = "Bence Csik"))

        assertThatExceptionOfType(FeignException.InternalServerError::class.java)
            .isThrownBy { client.createMember(CreateMember(url = "bcsik", name = "Bence Csik")) }
            .satisfies( { assertThat(it.status()).isEqualTo(500) } )
            .satisfies( { assertThat(it.contentUTF8()).contains(""","status":500,"error":"Internal Server Error","path":"/api/v1/member"}""") } )


        member.body?.let { client.removeMember(it.id) }
    }
}
