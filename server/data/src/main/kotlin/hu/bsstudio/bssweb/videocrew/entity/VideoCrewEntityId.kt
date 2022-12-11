package hu.bsstudio.bssweb.videocrew.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class VideoCrewEntityId(
    @Column(name = "video_id")
    var videoId: UUID,
    var position: String,
    @Column(name = "member_id")
    var memberId: UUID
) : Serializable
