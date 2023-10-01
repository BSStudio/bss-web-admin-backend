package hu.bsstudio.bssweb.metrics.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ContextConfiguration

@JsonTest
@ContextConfiguration(classes = [BssMetricsTest::class])
internal class BssMetricsTest(
    @Autowired private val underTest: JacksonTester<BssMetrics>
) {

    @Test
    internal fun `test serialisation`() {
        val actual = this.underTest.write(BSS_METRICS)

        actual.json shouldEqualJson JSON
    }

    @Test
    internal fun `test deserialization`() {
        val actual = this.underTest.parseObject(JSON)

        actual shouldBeEqual BSS_METRICS
    }

    private companion object {
        private const val videoCount = 10L
        private const val eventCount = 5L
        private const val memberCount = 100L
        private val BSS_METRICS = BssMetrics(
            videoCount = videoCount,
            eventCount = eventCount,
            memberCount = memberCount
        )
        private val JSON = """
            {
                "videoCount": $videoCount,
                "eventCount": $eventCount,
                "memberCount": $memberCount
            }
        """.trimIndent()
    }
}
