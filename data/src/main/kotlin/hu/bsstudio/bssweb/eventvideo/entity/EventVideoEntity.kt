package hu.bsstudio.bssweb.eventvideo.entity

import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@IdClass(EventVideoEntity::class)
@Table(name = "event_video")
data class EventVideoEntity(
    @Id
    @Column(name = "event_id")
    var eventId: UUID,
    @Id
    @Column(name = "video_id")
    var videoId: UUID
) : Serializable
