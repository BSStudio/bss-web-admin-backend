package hu.bsstudio.bssweb.videocrew.client

import hu.bsstudio.bssweb.videocrew.operation.VideoCrewOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "video-crew")
interface VideoCrewClient : VideoCrewOperation
