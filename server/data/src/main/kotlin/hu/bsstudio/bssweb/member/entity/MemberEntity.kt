package hu.bsstudio.bssweb.member.entity

import hu.bsstudio.bssweb.member.common.MemberStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "member")
data class MemberEntity(
    @Id
    var id: UUID = UUID.randomUUID(),
    var url: String,
    var name: String,
    var nickname: String = "",
    var description: String = "",
    var joinedAt: LocalDate = LocalDate.now(),
    var role: String = "",
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
    var archived: Boolean = false
)
