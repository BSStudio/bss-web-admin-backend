package hu.bsstudio.bssweb.member.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import hu.bsstudio.bssweb.member.common.MemberStatus
import java.time.LocalDate
import java.util.UUID

data class Member @JsonCreator constructor(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("url") val url: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("nickname") val nickname: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("joinedAt") val joinedAt: LocalDate,
    @JsonProperty("role") val role: String,
    @JsonProperty("status") val status: MemberStatus,
    @JsonProperty("archived") val archived: Boolean
)
