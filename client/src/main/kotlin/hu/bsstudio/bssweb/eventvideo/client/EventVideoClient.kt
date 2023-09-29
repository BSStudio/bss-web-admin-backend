package hu.bsstudio.bssweb.eventvideo.client

import hu.bsstudio.bssweb.eventvideo.operation.EventVideoOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "event-video", url = "\${bss.client.url}")
interface EventVideoClient : EventVideoOperation
