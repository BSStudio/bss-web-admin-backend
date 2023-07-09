package hu.bsstudio.bssweb.member.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class SimpleMember @JsonCreator constructor(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("name") val name: String,
    @JsonProperty("nickname") val nickname: String
)
