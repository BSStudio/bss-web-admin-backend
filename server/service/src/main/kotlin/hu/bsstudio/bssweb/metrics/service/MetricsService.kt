package hu.bsstudio.bssweb.metrics.service

import hu.bsstudio.bssweb.metrics.model.BssMetrics

interface MetricsService {
    fun getMetrics(): BssMetrics
}
