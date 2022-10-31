package hu.bsstudio.bssweb.video.entity

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

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
