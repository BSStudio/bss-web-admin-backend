package hu.bsstudio.bssweb.videocrew.client

import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "video-crew")
interface VideoCrewClient
