package hu.bsstudio.bssweb.video.client

import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "video")
interface VideoClient
