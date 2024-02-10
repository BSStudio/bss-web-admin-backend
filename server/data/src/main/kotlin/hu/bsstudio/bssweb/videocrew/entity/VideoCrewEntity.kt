package hu.bsstudio.bssweb.videocrew.entity

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "crew")
data class VideoCrewEntity(
    @EmbeddedId
    var id: VideoCrewEntityId,
)
