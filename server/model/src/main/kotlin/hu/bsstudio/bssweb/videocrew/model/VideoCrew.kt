package hu.bsstudio.bssweb.videocrew.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import hu.bsstudio.bssweb.member.model.SimpleMember
import java.util.UUID

data class VideoCrew @JsonCreator constructor(
    @JsonProperty("videoId") val videoId: UUID,
    @JsonProperty("position") val position: String,
    @JsonProperty("member") val member: SimpleMember
)
