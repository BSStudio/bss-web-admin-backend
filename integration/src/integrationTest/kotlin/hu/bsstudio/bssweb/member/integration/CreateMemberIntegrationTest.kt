package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpServerErrorException
import java.net.URI
import java.time.LocalDate

class CreateMemberIntegrationTest(
    @param:Autowired private val client: MemberClient,
    @param:Value("\${bss.client.url}") private val url: String,
) : IntegrationTest() {
    @Test
    internal fun `it should return 201 and member body`() {
        val actual = client.createMember(CREATE_MEMBER)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(201)
            body!! shouldBeEqual
                Member(
                    id = actual.body!!.id,
                    url = CREATE_MEMBER.url,
                    name = CREATE_MEMBER.name,
                    nickname = "",
                    description = "",
                    joinedAt = LocalDate.now(),
                    role = "",
                    status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
                    archived = false,
                )
            headers.location!! shouldBeEqual URI.create("$url/api/v1/member/${actual.body!!.id}")
        }
    }

    @Test
    internal fun `it should return 201 and member body on duplicate names`() {
        val actual1 = client.createMember(CREATE_MEMBER)
        val actual2 = client.createMember(CREATE_MEMBER.copy(url = "${CREATE_MEMBER.url}2"))

        assertSoftly(actual1) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(201)
            body!! shouldBeEqual
                Member(
                    id = actual1.body!!.id,
                    url = CREATE_MEMBER.url,
                    name = CREATE_MEMBER.name,
                    nickname = "",
                    description = "",
                    joinedAt = LocalDate.now(),
                    role = "",
                    status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
                    archived = false,
                )
            headers.location!! shouldBeEqual URI.create("$url/api/v1/member/${actual1.body!!.id}")
        }

        assertSoftly(actual2) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(201)
            body!! shouldBeEqual
                Member(
                    id = actual2.body!!.id,
                    url = "${CREATE_MEMBER.url}2",
                    name = CREATE_MEMBER.name,
                    nickname = "",
                    description = "",
                    joinedAt = LocalDate.now(),
                    role = "",
                    status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
                    archived = false,
                )
            headers.location!! shouldBeEqual URI.create("$url/api/v1/member/${actual2.body!!.id}")
        }
    }

    @Test
    internal fun `it should return 500 when duplicate urls were specified`() {
        memberRepository.save(DetailedMemberEntity(url = CREATE_MEMBER.url, name = CREATE_MEMBER.name))

        shouldThrow<HttpServerErrorException.InternalServerError> {
            client.createMember(CreateMember(url = CREATE_MEMBER.url, name = CREATE_MEMBER.name))
        } should {
            it.responseBodyAsString shouldContain ""","status":500,"error":"Internal Server Error""""
        }
    }

    private companion object {
        private val CREATE_MEMBER = CreateMember(url = "bcsik", name = "Bence Csik")
    }
}
