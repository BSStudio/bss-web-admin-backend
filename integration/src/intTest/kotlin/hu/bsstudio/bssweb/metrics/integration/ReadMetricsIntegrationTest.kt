package hu.bsstudio.bssweb.metrics.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.metrics.client.MetricsClient
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.video.model.CreateVideo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode

class ReadMetricsIntegrationTest(
    @Autowired private val client: MetricsClient,
    @Autowired private val videoClient: VideoClient,
    @Autowired private val eventClient: EventClient,
    @Autowired private val memberClient: MemberClient
) : IntegrationTest() {

    @BeforeEach
    fun setUp() {
        this.videoClient.getAllVideos().body?.forEach { this.videoClient.deleteVideo(it.id) }
        this.eventClient.findAllEvent().body?.forEach { this.eventClient.deleteEvent(it.id) }
        this.memberClient.getAllMembers().body?.forEach { this.memberClient.removeMember(it.id) }
    }

    @Test
    fun `it should return 200 and empty metrics`() {
        val actual = this.client.getMetrics()

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body!!).isEqualTo(BssMetrics(0, 0, 0))
    }

    @Test
    fun `it should return 200 and metrics with 1 for each value`() {
        this.videoClient.createVideo(CreateVideo("url", "title"))
        this.eventClient.createEvent(CreateEvent("url", "title"))
        this.memberClient.createMember(CreateMember("url", "name"))

        val actual = this.client.getMetrics()

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body!!).isEqualTo(BssMetrics(1, 1, 1))
    }
}
