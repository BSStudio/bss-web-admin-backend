package hu.bsstudio.bssweb.revision.mapper

import hu.bsstudio.bssweb.revision.entity.RevisionEntity
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import hu.bsstudio.bssweb.revision.model.RevisionType
import org.springframework.data.history.Revision
import org.springframework.data.history.RevisionMetadata
import org.springframework.data.history.Revisions
import java.time.Instant

class RevisionMapper {
    /**
     * Describes the [revisions] of one item, newest first. A delete and a restore are stored as updates of the item's
     * [deletedAt], so they are told apart from other updates by comparing it with the revision before.
     */
    fun <T : Any> revisionsToModel(
        revisions: Revisions<Long, T>,
        deletedAt: (T) -> Instant?,
    ): List<RevisionInfo> =
        revisions.content
            .mapIndexed { index, revision -> revisionToModel(revision, revisions.content.getOrNull(index - 1), deletedAt) }
            .reversed()

    private fun <T : Any> revisionToModel(
        revision: Revision<Long, T>,
        previous: Revision<Long, T>?,
        deletedAt: (T) -> Instant?,
    ) = RevisionInfo(
        number = revision.requiredRevisionNumber,
        timestamp = revision.requiredRevisionInstant,
        author = revision.metadata.getDelegate<RevisionEntity>().username,
        type = type(revision, previous, deletedAt),
    )

    private fun <T : Any> type(
        revision: Revision<Long, T>,
        previous: Revision<Long, T>?,
        deletedAt: (T) -> Instant?,
    ): RevisionType {
        val wasDeleted = previous?.let { deletedAt(it.entity) } != null
        val isDeleted = deletedAt(revision.entity) != null
        return when {
            revision.metadata.revisionType == RevisionMetadata.RevisionType.INSERT -> RevisionType.CREATED
            revision.metadata.revisionType == RevisionMetadata.RevisionType.DELETE -> RevisionType.DELETED
            !wasDeleted && isDeleted -> RevisionType.DELETED
            wasDeleted && !isDeleted -> RevisionType.RESTORED
            else -> RevisionType.UPDATED
        }
    }
}
