package hu.bsstudio.bssweb.video.entity

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import java.time.LocalDate
import java.util.UUID
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "video")
data class DetailedVideoEntity(
    @Id
    override var id: UUID,
    var url: String,
    var title: String,
    var description: String = "",
    var uploadedAt: LocalDate = LocalDate.now(),
    var visible: Boolean = false,
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id")
    var videoCrew: List<VideoCrewEntity> = listOf(),
) : SimpleVideoEntity
