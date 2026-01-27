package hu.bsstudio.bssweb.metrics.operation

import hu.bsstudio.bssweb.metrics.model.BssMetrics
import org.springframework.http.ResponseEntity
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange("/api/v1/metrics")
interface MetricsOperation {
    @GetExchange
    fun getMetrics(): ResponseEntity<BssMetrics>
}
