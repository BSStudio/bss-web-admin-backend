package hu.bsstudio.bssweb.event.client

import hu.bsstudio.bssweb.event.operation.EventOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "event")
interface EventClient : EventOperation
