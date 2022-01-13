package hu.bsstudio.bssweb.event.entity

import hu.bsstudio.bssweb.video.entity.VideoEntity
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "events")
data class DetailedEventEntity(
    @Id
    var id: String,
    var name: String,
    var description: String = "",
    var date: LocalDate = LocalDate.now(),
    var archived: Boolean = false,
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    var videos: List<VideoEntity> = listOf(),
)
