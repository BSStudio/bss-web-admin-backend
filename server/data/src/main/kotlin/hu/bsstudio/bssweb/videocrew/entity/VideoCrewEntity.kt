package hu.bsstudio.bssweb.videocrew.entity

import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

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
