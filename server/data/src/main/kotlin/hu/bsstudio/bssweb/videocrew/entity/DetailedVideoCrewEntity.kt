package hu.bsstudio.bssweb.videocrew.entity

import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.Hibernate

@Entity
@Table(name = "crew")
data class DetailedVideoCrewEntity(
    @EmbeddedId
    var id: VideoCrewEntityId,
) {

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    lateinit var member: SimpleMemberEntity

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as DetailedVideoCrewEntity
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id)"
    }
}
