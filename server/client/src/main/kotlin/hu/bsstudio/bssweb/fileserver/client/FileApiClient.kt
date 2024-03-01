package hu.bsstudio.bssweb.fileserver.client

import hu.bsstudio.bssweb.fileserver.model.MemberFileUpdate
import hu.bsstudio.bssweb.fileserver.model.VideoFileUpdate
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

@FeignClient(name = "member", url = "\${bss.file-api.url}")
interface FileApiClient {
    @PostMapping("/api/v1/member")
    fun createMemberFolder(fileUpdate: MemberFileUpdate): MemberFileUpdate

    @PutMapping("/api/v1/member")
    fun updateMemberFolder(fileUpdate: MemberFileUpdate): MemberFileUpdate

    @PostMapping("/api/v1/video")
    fun createVideoFolder(videoFileUpdate: VideoFileUpdate): VideoFileUpdate

    @PutMapping("/api/v1/video")
    fun updateVideoFolder(videoFileUpdate: VideoFileUpdate): VideoFileUpdate
}
