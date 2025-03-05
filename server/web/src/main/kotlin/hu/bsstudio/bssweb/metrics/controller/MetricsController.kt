package hu.bsstudio.bssweb.metrics.controller

import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.metrics.operation.MetricsOperation
import hu.bsstudio.bssweb.metrics.service.MetricsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class MetricsController(
    private val service: MetricsService,
) : MetricsOperation {
    override fun getMetrics(): ResponseEntity<BssMetrics> =
        service
            .getMetrics()
            .let { ResponseEntity.ok(it) }
}
