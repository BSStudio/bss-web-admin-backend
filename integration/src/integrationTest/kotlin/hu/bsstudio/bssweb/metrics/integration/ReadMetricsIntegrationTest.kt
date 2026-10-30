package hu.bsstudio.bssweb.metrics.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.metrics.client.MetricsClient
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.video.entity.VideoEntity
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode

class ReadMetricsIntegrationTest(
    @param:Autowired private val client: MetricsClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and empty metrics`() {
        val actual = this.client.getMetrics()

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual BssMetrics(0, 0, 0, 0)
        }
    }

    @Test
    internal fun `it should return 200 and metrics with 1 for each value`() {
        this.videoRepository.save(VideoEntity(title = "title"))
        this.eventRepository.save(EventEntity(url = "url", title = "title"))
        this.memberRepository.save(MemberEntity(url = "url", name = "name"))
        this.labelRepository.save(LabelEntity(name = "name", description = "description"))

        val actual = this.client.getMetrics()

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual BssMetrics(1, 1, 1, 1)
        }
    }
}
