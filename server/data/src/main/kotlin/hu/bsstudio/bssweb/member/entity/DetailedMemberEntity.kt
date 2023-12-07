package hu.bsstudio.bssweb.member.entity

import hu.bsstudio.bssweb.member.common.MemberStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "member")
data class DetailedMemberEntity(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as DetailedMemberEntity
        return this.id == other.id
    }

    override fun hashCode() = id.hashCode()
}
