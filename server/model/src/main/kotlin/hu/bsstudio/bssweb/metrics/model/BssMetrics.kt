package hu.bsstudio.bssweb.metrics.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class BssMetrics @JsonCreator constructor(
    @JsonProperty("videoCount") val videoCount: Long,
    @JsonProperty("eventCount") val eventCount: Long,
    @JsonProperty("memberCount") val memberCount: Long
)
