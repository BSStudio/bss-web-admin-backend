package hu.bsstudio.bssweb.video.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.UUID

data class Video @JsonCreator constructor(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("url") val url: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("uploadedAt") val uploadedAt: LocalDate,
    @JsonProperty("visible") val visible: Boolean
)
