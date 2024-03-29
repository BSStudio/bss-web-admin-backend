package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

class ArchiveMemberIntegrationTest(
    @Autowired private val client: MemberClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and archive member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.archiveMembers(listOf(entity.id), true)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body.shouldContainExactly(entity.id)
        }
    }

    @Test
    internal fun `it should return 200 and unarchive member`() {
        val entity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name", archived = true))

        val actual = client.archiveMembers(listOf(entity.id), false)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body.shouldContainExactly(entity.id)
        }
    }

    @Test
    internal fun `it should return 200 and empty list when id can't be found`() {
        val actual = client.archiveMembers(listOf(UUID.fromString("00000000-0000-0000-0000-000000000000")), true)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body.shouldBeEmpty()
        }
    }
}
