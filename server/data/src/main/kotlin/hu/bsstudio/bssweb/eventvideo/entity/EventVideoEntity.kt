package hu.bsstudio.bssweb.eventvideo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.util.UUID

@Entity
@IdClass(EventVideoEntity::class)
@Table(name = "event_video")
data class EventVideoEntity(
    @Id
    @Column(name = "event_id")
    var eventId: UUID,
    @Id
    @Column(name = "video_id")
    var videoId: UUID,
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
}
