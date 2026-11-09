package hu.bsstudio.bssweb.revision.model

import java.time.Instant

/** One change of a video, an event or a member. */
data class RevisionInfo(
    val number: Long,
    val timestamp: Instant,
    /** Who made the change, or `null` if nobody was signed in. */
    val author: String?,
    val type: RevisionType,
)
