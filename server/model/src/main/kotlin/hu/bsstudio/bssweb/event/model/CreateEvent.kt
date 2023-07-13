package hu.bsstudio.bssweb.event.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CreateEvent @JsonCreator constructor(
    @JsonProperty("url") val url: String,
    @JsonProperty("title") val title: String
)
