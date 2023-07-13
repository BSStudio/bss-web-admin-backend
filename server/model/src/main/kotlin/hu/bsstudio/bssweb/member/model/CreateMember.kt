package hu.bsstudio.bssweb.member.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CreateMember @JsonCreator constructor(
    @JsonProperty("url") val url: String,
    @JsonProperty("name") val name: String
)
