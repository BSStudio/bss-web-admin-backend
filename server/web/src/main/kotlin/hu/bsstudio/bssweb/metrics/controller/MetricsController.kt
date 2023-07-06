package hu.bsstudio.bssweb.metrics.controller

import hu.bsstudio.bssweb.metrics.api.MetricsApi
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.metrics.service.MetricsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/metrics")
class MetricsController(private val service: MetricsService): MetricsApi {

    override fun getMetrics(): ResponseEntity<BssMetrics> {
        return service.getMetrics()
            .let { ResponseEntity.ok(it) }
    }
}
