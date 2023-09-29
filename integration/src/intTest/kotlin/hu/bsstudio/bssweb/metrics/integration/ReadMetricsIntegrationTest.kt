package hu.bsstudio.bssweb.metrics.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.metrics.client.MetricsClient
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode

class ReadMetricsIntegrationTest(
    @Autowired private val client: MetricsClient
) : IntegrationTest() {

    @Test
    fun `it should return 200 and empty metrics`() {
        val actual = this.client.getMetrics()

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body!!).isEqualTo(BssMetrics(0, 0, 0))
    }

    @Test
    fun `it should return 200 and metrics with 1 for each value`() {
        this.videoRepository.save(DetailedVideoEntity(url = "url", title = "title"))
        this.eventRepository.save(DetailedEventEntity(url = "url", title = "title"))
        this.memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = this.client.getMetrics()

        assertThat(actual.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        assertThat(actual.body!!).isEqualTo(BssMetrics(1, 1, 1))
    }
}
