package hu.bsstudio.bssweb.video.client

import hu.bsstudio.bssweb.video.operation.VideoOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "video")
interface VideoClient : VideoOperation
