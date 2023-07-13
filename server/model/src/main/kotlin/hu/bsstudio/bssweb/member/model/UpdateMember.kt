package hu.bsstudio.bssweb.member.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import hu.bsstudio.bssweb.member.common.MemberStatus
import java.time.LocalDate

data class UpdateMember @JsonCreator constructor(
    @JsonProperty("url") val url: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("nickname") val nickname: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("joinedAt") val joinedAt: LocalDate,
    @JsonProperty("role") val role: String,
    @JsonProperty("status") val status: MemberStatus,
    @JsonProperty("archived") val archived: Boolean
)
