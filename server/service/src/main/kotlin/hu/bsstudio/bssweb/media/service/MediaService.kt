package hu.bsstudio.bssweb.media.service

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/** Uploads images to the file API, which converts them to the sizes and formats the websites use. */
interface MediaService {
    fun uploadVideoThumbnail(
        videoId: UUID,
        image: MultipartFile,
    )

    fun uploadMemberPicture(
        memberId: UUID,
        image: MultipartFile,
    )
}
