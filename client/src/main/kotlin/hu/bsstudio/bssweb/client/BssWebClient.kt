package hu.bsstudio.bssweb.client

import hu.bsstudio.bssweb.event.operation.EventOperation
import hu.bsstudio.bssweb.eventvideo.operation.EventVideoOperation
import hu.bsstudio.bssweb.member.operation.MemberOperation
import hu.bsstudio.bssweb.metrics.operation.MetricsOperation
import hu.bsstudio.bssweb.video.operation.VideoOperation
import hu.bsstudio.bssweb.videocrew.operation.VideoCrewOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "bss-web")
interface BssWebClient :
        EventOperation,
        EventVideoOperation,
        MemberOperation,
        MetricsOperation,
        VideoOperation,
        VideoCrewOperation
