package hu.bsstudio.bssweb.member.model

import java.util.UUID

data class SimpleMember(
    val id: UUID,
    val name: String,
    val nickname: String
)
