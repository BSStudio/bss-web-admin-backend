package hu.bsstudio.bssweb.member.model

import hu.bsstudio.bssweb.member.common.MemberStatus
import java.time.LocalDate

data class UpdateMember(
    val url: String,
    val name: String,
    val nickname: String,
    val description: String,
    val joinedAt: LocalDate,
    val role: String,
    val status: MemberStatus,
    val archived: Boolean
)
