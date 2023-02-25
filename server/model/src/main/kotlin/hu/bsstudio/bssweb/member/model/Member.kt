package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.member.common.MemberStatus
import java.time.LocalDate
import java.util.UUID

data class Member(
    val id: UUID,
    val url: String,
    val name: String,
    val nickname: String,
    val description: String,
    val joinedAt: LocalDate,
    val role: String,
    val status: MemberStatus,
    val archived: Boolean
)
