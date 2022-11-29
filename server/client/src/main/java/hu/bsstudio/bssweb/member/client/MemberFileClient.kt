package hu.bsstudio.bssweb.member.client

import hu.bsstudio.bssweb.member.model.MemberFile
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient("member")
interface MemberFileClient {

    @RequestMapping(method = [RequestMethod.POST], path = ["/api/v1/member"])
    fun createFile(memberFile: MemberFile): MemberFile

    @RequestMapping(method = [RequestMethod.PUT], path = ["/api/v1/member"])
    fun updateFile(memberFile: MemberFile): MemberFile
}
