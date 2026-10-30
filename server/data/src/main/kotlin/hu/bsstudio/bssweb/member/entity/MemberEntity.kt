package hu.bsstudio.bssweb.member.entity

import hu.bsstudio.bssweb.member.common.MemberStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.envers.AuditTable
import org.hibernate.envers.Audited
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "member")
@Audited
@AuditTable("member_aud")
@SQLRestriction("deleted_at is null")
data class MemberEntity(
    var url: String,
    var name: String,
    var nickname: String = "",
    var description: String = "",
    var joinedAt: LocalDate = LocalDate.now(),
    var role: String = "",
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
    var archived: Boolean = false,
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID

    @CreationTimestamp
    lateinit var createdAt: Instant

    @UpdateTimestamp
    lateinit var updatedAt: Instant

    /** When the member was deleted, or `null`. A deleted member stays in the table; the restriction above hides it. */
    var deletedAt: Instant? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as MemberEntity
        return this.id == other.id
    }

    override fun hashCode() = id.hashCode()
}
