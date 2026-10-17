package hu.bsstudio.bssweb.fileserver.client

import hu.bsstudio.bssweb.fileserver.model.MemberFileUpdate
import hu.bsstudio.bssweb.fileserver.model.VideoFileUpdate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.annotation.PutExchange
import java.util.UUID

@HttpExchange("/api/v1")
interface FileApiClient {
    @PostExchange("/member")
    fun createMemberFolder(
        @RequestBody fileUpdate: MemberFileUpdate,
    ): MemberFileUpdate

    @PutExchange("/member")
    fun updateMemberFolder(
        @RequestBody fileUpdate: MemberFileUpdate,
    ): MemberFileUpdate

    @PostExchange("/video")
    fun createVideoFolder(
        @RequestBody videoFileUpdate: VideoFileUpdate,
    ): VideoFileUpdate

    @PutExchange("/video")
    fun updateVideoFolder(
        @RequestBody videoFileUpdate: VideoFileUpdate,
    ): VideoFileUpdate

    /** The file API converts the image to the thumbnail sizes and formats under /v/{videoId}/thumbnail. */
    @PostExchange("/video/{videoId}/thumbnail", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
    fun uploadVideoThumbnail(
        @PathVariable videoId: UUID,
        @RequestPart("file") image: MultipartFile,
    )

    /** The file API converts the image to the profile picture sizes and formats under /m/{memberId}/profile. */
    @PostExchange("/member/{memberId}/profilePicture", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
    fun uploadMemberPicture(
        @PathVariable memberId: UUID,
        @RequestPart("file") image: MultipartFile,
    )
}
