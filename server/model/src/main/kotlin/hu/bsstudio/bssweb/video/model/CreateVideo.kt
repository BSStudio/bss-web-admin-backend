package hu.bsstudio.bssweb.video.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CreateVideo @JsonCreator constructor(
    @JsonProperty("url") val url: String,
    @JsonProperty("title") val title: String
)
