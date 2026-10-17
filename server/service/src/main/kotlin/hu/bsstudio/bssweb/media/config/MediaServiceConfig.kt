package hu.bsstudio.bssweb.media.config

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.media.service.DefaultMediaService
import hu.bsstudio.bssweb.media.service.MediaService
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.video.service.VideoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MediaServiceConfig {
    @Bean
    fun mediaService(
        fileClient: FileApiClient,
        videoService: VideoService,
        memberService: MemberService,
    ): MediaService = DefaultMediaService(fileClient, videoService, memberService)
}
