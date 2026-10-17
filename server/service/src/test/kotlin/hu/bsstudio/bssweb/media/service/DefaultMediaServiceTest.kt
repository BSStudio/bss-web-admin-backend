package hu.bsstudio.bssweb.media.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.MemberFileUpdate
import hu.bsstudio.bssweb.fileserver.model.VideoFileUpdate
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.service.VideoService
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultMediaServiceTest(
    @param:MockK private val fileClient: FileApiClient,
    @param:MockK private val videoService: VideoService,
    @param:MockK private val memberService: MemberService,
) {
    @InjectMockKs
    private lateinit var underTest: DefaultMediaService

    @Test
    internal fun `should upload a video thumbnail`() {
        every { fileClient.uploadVideoThumbnail(VIDEO.id, IMAGE) } just runs

        underTest.uploadVideoThumbnail(VIDEO.id, IMAGE)

        verify(exactly = 1) { fileClient.uploadVideoThumbnail(VIDEO.id, IMAGE) }
        verify(exactly = 0) { fileClient.createVideoFolder(any()) }
    }

    @Test
    internal fun `should create the video folder when it is missing and upload again`() {
        every { fileClient.uploadVideoThumbnail(VIDEO.id, IMAGE) } throws NOT_FOUND andThen Unit
        every { videoService.findVideoById(VIDEO.id) } returns Optional.of(VIDEO)
        every { fileClient.createVideoFolder(VideoFileUpdate(VIDEO.id, VIDEO.urls)) } returns VideoFileUpdate(VIDEO.id, VIDEO.urls)

        underTest.uploadVideoThumbnail(VIDEO.id, IMAGE)

        verify(exactly = 2) { fileClient.uploadVideoThumbnail(VIDEO.id, IMAGE) }
    }

    @Test
    internal fun `should pass other file API errors on`() {
        every { fileClient.uploadVideoThumbnail(VIDEO.id, IMAGE) } throws HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)

        shouldThrow<HttpServerErrorException> { underTest.uploadVideoThumbnail(VIDEO.id, IMAGE) }
    }

    @Test
    internal fun `should upload a member picture`() {
        every { fileClient.uploadMemberPicture(MEMBER.id, IMAGE) } just runs

        underTest.uploadMemberPicture(MEMBER.id, IMAGE)

        verify(exactly = 1) { fileClient.uploadMemberPicture(MEMBER.id, IMAGE) }
    }

    @Test
    internal fun `should create the member folder when it is missing and upload again`() {
        every { fileClient.uploadMemberPicture(MEMBER.id, IMAGE) } throws NOT_FOUND andThen Unit
        every { memberService.findMemberById(MEMBER.id) } returns Optional.of(MEMBER)
        every { fileClient.createMemberFolder(MemberFileUpdate(MEMBER.id, MEMBER.url)) } returns MemberFileUpdate(MEMBER.id, MEMBER.url)

        underTest.uploadMemberPicture(MEMBER.id, IMAGE)

        verify(exactly = 2) { fileClient.uploadMemberPicture(MEMBER.id, IMAGE) }
    }

    private companion object {
        private val IMAGE = mockk<MultipartFile>()
        private val NOT_FOUND = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", HttpHeaders.EMPTY, ByteArray(0), null)
        private val VIDEO =
            DetailedVideo(
                id = UUID.randomUUID(),
                title = "Gólyatábor 2026 - Vám",
                urls = listOf("golyatabor-2026-vam"),
                description = "",
                shootingDateStart = LocalDate.of(2026, 8, 13),
                shootingDateEnd = LocalDate.of(2026, 8, 13),
                visible = true,
                labels = emptyList(),
                crew = emptyList(),
            )
        private val MEMBER =
            Member(
                id = UUID.randomUUID(),
                url = "kovacs-anna",
                name = "Kovács Anna",
                nickname = "",
                description = "",
                joinedAt = LocalDate.of(2024, 9, 1),
                role = "",
                status = MemberStatus.MEMBER,
                archived = false,
            )
    }
}
