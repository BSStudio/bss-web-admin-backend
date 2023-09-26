package hu.bsstudio.bssweb.metrics.client

import hu.bsstudio.bssweb.metrics.operation.MetricsOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "metrics")
interface MetricsClient : MetricsOperation
