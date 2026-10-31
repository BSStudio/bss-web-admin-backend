package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.revision.model.RevisionType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpClientErrorException
import java.util.UUID

class RestoreMemberIntegrationTest(
    @param:Autowired private val client: MemberClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and the restored member`() {
        val id = create()
        client.removeMember(id)

        val actual = client.restoreMember(id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        actual.body!!.name shouldBeEqual NAME
        memberRepository.findById(id).shouldBePresent()
    }

    @Test
    internal fun `it should return 404 when no deleted member has the id`() {
        val id = create()

        shouldThrow<HttpClientErrorException.NotFound> { client.restoreMember(id) }
    }

    @Test
    internal fun `it should return 409 when another member has taken its url`() {
        val id = create()
        client.removeMember(id)
        create()

        shouldThrow<HttpClientErrorException.Conflict> { client.restoreMember(id) }
    }

    @Test
    internal fun `it should list the revisions and restore an earlier one`() {
        val id = create()
        val current = client.getMemberById(id).body!!
        client.updateMember(
            id,
            UpdateMember(
                url = current.url,
                name = NEW_NAME,
                nickname = current.nickname,
                description = current.description,
                joinedAt = current.joinedAt,
                role = current.role,
                status = current.status,
                archived = current.archived,
            ),
        )
        client.removeMember(id)
        client.restoreMember(id)

        val revisions = client.getMemberRevisions(id).body!!
        val first = revisions.last().number
        val actual = client.restoreMemberRevision(id, first)

        revisions.map { it.type } shouldContainExactly
            listOf(RevisionType.RESTORED, RevisionType.DELETED, RevisionType.UPDATED, RevisionType.CREATED)
        client.getMemberRevision(id, first).body!!.name shouldBeEqual NAME
        actual.body!!.name shouldBeEqual NAME
        client.getMemberById(id).body!!.name shouldBeEqual NAME
    }

    @Test
    internal fun `it should return 404 when the member to roll back is missing`() {
        shouldThrow<HttpClientErrorException.NotFound> {
            client.restoreMemberRevision(UUID.fromString("00000000-0000-0000-0000-000000000000"), 1)
        }
    }

    private fun create() = client.createMember(CreateMember(url = URL, name = NAME)).body!!.id

    private companion object {
        private const val URL = "url"
        private const val NAME = "name"
        private const val NEW_NAME = "newName"
    }
}
