package hu.bsstudio.bssweb.eventvideo.client

import hu.bsstudio.bssweb.eventvideo.operation.EventVideoOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "event-video")
interface EventVideoClient : EventVideoOperation
