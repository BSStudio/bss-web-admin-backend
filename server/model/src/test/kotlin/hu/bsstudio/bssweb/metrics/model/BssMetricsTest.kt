package hu.bsstudio.bssweb.metrics.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
internal class BssMetricsTest(
    @param:Autowired private val underTest: JacksonTester<BssMetrics>,
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
        private const val VIDEO_COUNT = 10L
        private const val EVENT_COUNT = 5L
        private const val MEMBER_COUNT = 100L
        private val BSS_METRICS =
            BssMetrics(
                videoCount = VIDEO_COUNT,
                eventCount = EVENT_COUNT,
                memberCount = MEMBER_COUNT,
            )
        private val JSON =
            """
            {
                "videoCount": $VIDEO_COUNT,
                "eventCount": $EVENT_COUNT,
                "memberCount": $MEMBER_COUNT
            }
            """.trimIndent()
    }
}
