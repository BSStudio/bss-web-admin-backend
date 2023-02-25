package hu.bsstudio.bssweb.metrics.controller

import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.metrics.service.MetricsService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus

@ExtendWith(MockKExtension::class)
internal class MetricsControllerTest {

    @MockK
    private lateinit var service: MetricsService

    @InjectMockKs
    private lateinit var underTest: MetricsController

    @Test
    internal fun `should return ok and metrics`() {
        every { service.getMetrics() } returns METRICS

        val response = underTest.getMetrics()

        assertThat(response.body).isEqualTo(METRICS)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    private companion object {
        private val METRICS = mockk<BssMetrics>()
    }
}
