package hu.bsstudio.bssweb.video.client

import hu.bsstudio.bssweb.video.model.VideoFile
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient("video")
interface VideoFileClient {

    @RequestMapping(method = [RequestMethod.POST], path = ["/api/v1/video"])
    fun createFile(videoFile: VideoFile): VideoFile

    @RequestMapping(method = [RequestMethod.PUT], path = ["/api/v1/video"])
    fun updateFile(videoFile: VideoFile): VideoFile
}
