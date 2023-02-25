package hu.bsstudio.bssweb.videocrew.entity

import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "crew")
data class DetailedVideoCrewEntity(
    @EmbeddedId
    var id: VideoCrewEntityId,
    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    var member: SimpleMemberEntity
)
