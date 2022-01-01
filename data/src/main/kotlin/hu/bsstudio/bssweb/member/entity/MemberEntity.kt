package hu.bsstudio.bssweb.member.entity

import hu.bsstudio.bssweb.member.common.MemberStatus
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity(name = "members")
data class MemberEntity(
    @Id
    val id: UUID,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val role: String,
    @Enumerated(EnumType.STRING)
    val status: MemberStatus,
)
