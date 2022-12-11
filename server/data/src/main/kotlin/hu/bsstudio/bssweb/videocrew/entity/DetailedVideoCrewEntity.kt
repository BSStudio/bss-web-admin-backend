package hu.bsstudio.bssweb.videocrew.entity

import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import jakarta.persistence.*

@Entity
@Table(name = "crew")
data class DetailedVideoCrewEntity(
    @EmbeddedId
    var id: VideoCrewEntityId,
    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    var member: SimpleMemberEntity,
)
