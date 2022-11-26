package hu.bsstudio.bssweb.videocrew.entity

import java.io.Serializable
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table

@Entity
@IdClass(VideoCrewEntity::class)
@Table(name = "crew")
data class VideoCrewEntity(
    @Id
    @Column(name = "video_id")
    var videoId: UUID,
    @Id
    var position: String,
    @Id
    var memberId: UUID
) : Serializable
