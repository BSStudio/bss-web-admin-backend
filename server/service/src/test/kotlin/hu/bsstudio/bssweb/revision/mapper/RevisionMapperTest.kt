package hu.bsstudio.bssweb.revision.mapper

import hu.bsstudio.bssweb.revision.entity.RevisionEntity
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import hu.bsstudio.bssweb.revision.model.RevisionType
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.data.history.Revision
import org.springframework.data.history.RevisionMetadata
import org.springframework.data.history.Revisions
import java.time.Instant
import java.util.Optional

internal class RevisionMapperTest {
    private val underTest = RevisionMapper()

    @Test
    internal fun `should tell creates, updates, deletes and restores apart, newest first`() {
        val revisions =
            Revisions.of(
                listOf(
                    revision(1, RevisionMetadata.RevisionType.INSERT, null),
                    revision(2, RevisionMetadata.RevisionType.UPDATE, null),
                    revision(3, RevisionMetadata.RevisionType.UPDATE, DELETED_AT),
                    revision(4, RevisionMetadata.RevisionType.UPDATE, DELETED_AT),
                    revision(5, RevisionMetadata.RevisionType.UPDATE, null),
                    revision(6, RevisionMetadata.RevisionType.DELETE, null),
                ),
            )

        val actual = underTest.revisionsToModel(revisions, Item::deletedAt)

        actual.shouldContainExactly(
            info(6, RevisionType.DELETED),
            info(5, RevisionType.RESTORED),
            info(4, RevisionType.UPDATED),
            info(3, RevisionType.DELETED),
            info(2, RevisionType.UPDATED),
            info(1, RevisionType.CREATED),
        )
    }

    @Test
    internal fun `should take an update as the first revision for an update`() {
        val revisions = Revisions.of(listOf(revision(7, RevisionMetadata.RevisionType.UPDATE, null, author = null)))

        val actual = underTest.revisionsToModel(revisions, Item::deletedAt)

        actual.shouldContainExactly(RevisionInfo(7, TIMESTAMP, null, RevisionType.UPDATED))
    }

    @Test
    internal fun `should map no revisions to an empty list`() {
        val actual = underTest.revisionsToModel(Revisions.none<Long, Item>(), Item::deletedAt)

        actual.shouldBeEmpty()
    }

    private data class Item(
        val deletedAt: Instant?,
    )

    private companion object {
        private const val AUTHOR = "author"
        private val TIMESTAMP = Instant.parse("2026-10-30T12:00:00Z")
        private val DELETED_AT = Instant.parse("2026-10-30T13:00:00Z")

        private fun info(
            number: Long,
            type: RevisionType,
        ) = RevisionInfo(number, TIMESTAMP, AUTHOR, type)

        private fun revision(
            number: Long,
            type: RevisionMetadata.RevisionType,
            deletedAt: Instant?,
            author: String? = AUTHOR,
        ): Revision<Long, Item> {
            val metadata = mockk<RevisionMetadata<Long>>()
            every { metadata.revisionNumber } returns Optional.of(number)
            every { metadata.requiredRevisionNumber } returns number
            every { metadata.requiredRevisionInstant } returns TIMESTAMP
            every { metadata.revisionType } returns type
            every { metadata.getDelegate<RevisionEntity>() } returns RevisionEntity().apply { username = author }
            return Revision.of(metadata, Item(deletedAt))
        }
    }
}
