package hu.bsstudio.bssweb.media.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.MemberFileUpdate
import hu.bsstudio.bssweb.fileserver.model.VideoFileUpdate
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.video.service.VideoService
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

class DefaultMediaService(
    private val fileClient: FileApiClient,
    private val videoService: VideoService,
    private val memberService: MemberService,
) : MediaService {
    override fun uploadVideoThumbnail(
        videoId: UUID,
        image: MultipartFile,
    ) = uploadCreatingFolder(
        upload = { fileClient.uploadVideoThumbnail(videoId, image) },
        createFolder = {
            val video = videoService.findVideoById(videoId).orElseThrow()
            fileClient.createVideoFolder(VideoFileUpdate(video.id, video.urls))
        },
    )

    override fun uploadMemberPicture(
        memberId: UUID,
        image: MultipartFile,
    ) = uploadCreatingFolder(
        upload = { fileClient.uploadMemberPicture(memberId, image) },
        createFolder = {
            val member = memberService.findMemberById(memberId).orElseThrow()
            fileClient.createMemberFolder(MemberFileUpdate(member.id, member.url))
        },
    )

    // Videos and members created before the file API existed have no folder yet,
    // and the file API answers 404 for them. Create the folder and try once more.
    private fun uploadCreatingFolder(
        upload: () -> Unit,
        createFolder: () -> Unit,
    ) {
        try {
            upload()
        } catch (_: HttpClientErrorException.NotFound) {
            createFolder()
            upload()
        }
    }
}
