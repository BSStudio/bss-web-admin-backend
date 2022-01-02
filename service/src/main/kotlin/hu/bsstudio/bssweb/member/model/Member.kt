package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.member.common.MemberStatus
import java.time.LocalDate

data class Member(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val joinedAt: LocalDate,
    val role: String,
    val status: MemberStatus,
    val archived: Boolean,
)
