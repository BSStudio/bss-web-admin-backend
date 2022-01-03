package hu.bsstudio.bssweb.videocrew.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@IdClass(value = VideoCrewEntity::class)
@Table(name = "crew")
data class VideoCrewEntity(
    @Id
    @Column(name = "video_id")
    var videoId: String,
    @Id
    var position: String,
    @Id
    var memberId: String
) : Serializable
