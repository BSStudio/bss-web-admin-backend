package hu.bsstudio.bssweb.videocrew.model

import java.util.UUID

data class VideoCrewRequest(
    val videoId: UUID,
    val position: String,
    val memberId: UUID,
)
