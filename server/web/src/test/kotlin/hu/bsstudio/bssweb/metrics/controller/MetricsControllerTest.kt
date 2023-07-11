package hu.bsstudio.bssweb.metrics.controller

import com.fasterxml.jackson.databind.ObjectMapper
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.metrics.service.MetricsService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(MetricsController::class)
@ContextConfiguration(classes = [MetricsControllerTest::class])
internal class MetricsControllerTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockK
    private lateinit var service: MetricsService

    @Test
    internal fun `should return ok and metrics`() {
        every { service.getMetrics() } returns METRICS

        mockMvc.get("/api/v1/metrics").andExpectAll {
            status { isOk() }
            content { objectMapper.writeValueAsString(METRICS) }
        }
    }

    private companion object {
        private val METRICS = BssMetrics(videoCount = 1, eventCount = 1, memberCount = 1)
    }
}
