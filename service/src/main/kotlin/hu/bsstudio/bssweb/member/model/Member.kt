package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.member.common.MemberStatus
import java.util.UUID

data class Member(
    val id: UUID,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val role: String,
    val status: MemberStatus,
)
