package hu.bsstudio.bssweb.videocrew.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class VideoCrewRequest @JsonCreator constructor(
    @JsonProperty("videoId") val videoId: UUID,
    @JsonProperty("position") val position: String,
    @JsonProperty("memberId") val memberId: UUID
)
