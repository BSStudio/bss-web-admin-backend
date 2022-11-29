package hu.bsstudio.bssweb.fileserver.client

import hu.bsstudio.bssweb.fileserver.model.FileUpdate
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient("member")
interface FileApiClient {

    @RequestMapping(method = [RequestMethod.POST], path = ["/api/v1/member"])
    fun createMemberFolder(fileUpdate: FileUpdate): FileUpdate

    @RequestMapping(method = [RequestMethod.PUT], path = ["/api/v1/member"])
    fun updateMemberFolder(fileUpdate: FileUpdate): FileUpdate

    @RequestMapping(method = [RequestMethod.POST], path = ["/api/v1/video"])
    fun createVideoFolder(fileUpdate: FileUpdate): FileUpdate

    @RequestMapping(method = [RequestMethod.PUT], path = ["/api/v1/video"])
    fun updateVideoFolder(fileUpdate: FileUpdate): FileUpdate
}
