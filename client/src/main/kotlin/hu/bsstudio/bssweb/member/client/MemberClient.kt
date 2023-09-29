package hu.bsstudio.bssweb.member.client

import hu.bsstudio.bssweb.member.operation.MemberOperation
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "member", url = "\${bss.client.url}")
interface MemberClient : MemberOperation
