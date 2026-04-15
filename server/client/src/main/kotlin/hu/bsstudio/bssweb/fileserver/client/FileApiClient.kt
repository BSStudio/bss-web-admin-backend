package hu.bsstudio.bssweb.fileserver.client

import hu.bsstudio.bssweb.fileserver.model.MemberFileUpdate
import hu.bsstudio.bssweb.fileserver.model.VideoFileUpdate
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.annotation.PutExchange

@HttpExchange("/api/v1")
interface FileApiClient {
    @PostExchange("/member")
    fun createMemberFolder(fileUpdate: MemberFileUpdate): MemberFileUpdate

    @PutExchange("/member")
    fun updateMemberFolder(fileUpdate: MemberFileUpdate): MemberFileUpdate

    @PostExchange("/video")
    fun createVideoFolder(videoFileUpdate: VideoFileUpdate): VideoFileUpdate

    @PutExchange("/video")
    fun updateVideoFolder(videoFileUpdate: VideoFileUpdate): VideoFileUpdate
}
