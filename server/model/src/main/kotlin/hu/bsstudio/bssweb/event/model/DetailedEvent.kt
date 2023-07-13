package hu.bsstudio.bssweb.event.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import hu.bsstudio.bssweb.video.model.Video
import java.time.LocalDate
import java.util.UUID

data class DetailedEvent @JsonCreator constructor(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("url") val url: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("date") val date: LocalDate,
    @JsonProperty("visible") val visible: Boolean,
    @JsonProperty("videos") val videos: List<Video>
)
