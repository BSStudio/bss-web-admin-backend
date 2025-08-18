package hu.bsstudio.bssweb.metrics.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.metrics.service.MetricsService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(MetricsController::class)
internal class MetricsControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val objectMapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var service: MetricsService

    @Test
    internal fun `should return ok and metrics`() {
        every { service.getMetrics() } returns METRICS

        mockMvc
            .get(BASE_URL) {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(METRICS) }
            }
    }

    private companion object {
        private const val BASE_URL = "/api/v1/metrics"
        private const val USERNAME = "user"
        private const val PASSWORD = "password"
        private val METRICS = BssMetrics(videoCount = 1, eventCount = 1, memberCount = 1)
    }
}
