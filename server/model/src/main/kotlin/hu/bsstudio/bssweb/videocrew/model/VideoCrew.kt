package hu.bsstudio.bssweb.videocrew.model

import hu.bsstudio.bssweb.member.model.SimpleMember
import java.util.UUID

data class VideoCrew(
    val videoId: UUID,
    val position: String,
    val member: SimpleMember
)
