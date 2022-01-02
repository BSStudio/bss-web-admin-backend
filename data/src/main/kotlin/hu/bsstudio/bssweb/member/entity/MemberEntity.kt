package hu.bsstudio.bssweb.member.entity

import hu.bsstudio.bssweb.member.common.MemberStatus
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity(name = "members")
data class MemberEntity(
    @Id
    var id: String,
    var name: String,
    var description: String = "",
    var imageUrl: String? = null,
    var joinedAt: LocalDate = LocalDate.now(),
    var role: String = "",
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
    var archived: Boolean = false,
)
