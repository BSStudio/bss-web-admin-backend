package hu.bsstudio.bssweb.event.entity

import hu.bsstudio.bssweb.video.entity.VideoEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "event")
data class DetailedEventEntity(
    @Id
    var id: UUID,
    var url: String,
    var title: String,
    var description: String = "",
    var date: LocalDate = LocalDate.now(),
    var visible: Boolean = false,
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "event_video",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "video_id")]
    )
    var videos: List<VideoEntity> = listOf(),
)
