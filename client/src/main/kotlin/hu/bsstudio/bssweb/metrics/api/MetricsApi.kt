package hu.bsstudio.bssweb.metrics.api

import hu.bsstudio.bssweb.metrics.model.BssMetrics
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/v1/metrics")
interface MetricsApi {

    @GetMapping
    fun getMetrics(): ResponseEntity<BssMetrics>

}
