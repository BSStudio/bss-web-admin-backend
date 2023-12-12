package hu.bsstudio.bssweb.fileserver.client

import hu.bsstudio.bssweb.fileserver.model.FileUpdate
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

@FeignClient(name = "member", url = "\${bss.file-api.url}")
interface FileApiClient {
    @PostMapping("/api/v1/member")
    fun createMemberFolder(fileUpdate: FileUpdate): FileUpdate

    @PutMapping("/api/v1/member")
    fun updateMemberFolder(fileUpdate: FileUpdate): FileUpdate

    @PostMapping("/api/v1/video")
    fun createVideoFolder(fileUpdate: FileUpdate): FileUpdate

    @PutMapping("/api/v1/video")
    fun updateVideoFolder(fileUpdate: FileUpdate): FileUpdate
}
