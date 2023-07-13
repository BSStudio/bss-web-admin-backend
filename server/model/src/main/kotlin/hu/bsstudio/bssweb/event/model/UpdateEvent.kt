package hu.bsstudio.bssweb.event.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class UpdateEvent @JsonCreator constructor(
    @JsonProperty("url") val url: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("date") val date: LocalDate,
    @JsonProperty("visible") val visible: Boolean
)
