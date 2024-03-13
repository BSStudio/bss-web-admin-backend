package hu.bsstudio.bssweb.label.client

import hu.bsstudio.bssweb.label.operation.LabelOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "label", url = "\${bss.client.url}")
interface LabelClient : LabelOperation
