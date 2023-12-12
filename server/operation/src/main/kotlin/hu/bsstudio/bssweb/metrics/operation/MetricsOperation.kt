package hu.bsstudio.bssweb.metrics.operation

import hu.bsstudio.bssweb.metrics.model.BssMetrics
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

interface MetricsOperation {
    @GetMapping("/api/v1/metrics")
    fun getMetrics(): ResponseEntity<BssMetrics>
}
