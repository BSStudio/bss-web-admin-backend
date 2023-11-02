package hu.bsstudio.bssweb.videocrew.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.model.SimpleMember
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.client.VideoCrewClient
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrewRequest
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AddVideoCrewIntegrationTest(
    @Autowired private val client: VideoCrewClient
) : IntegrationTest() {

    @Test
    fun `it should add a member to a video`() {
        val videoEntity = videoRepository.save(DetailedVideoEntity(url = "url", title = "title"))
        val memberEntity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))

        val actual = client.addPosition(VideoCrewRequest(videoEntity.id, "position", memberEntity.id))

        assertSoftly(actual) {
            statusCode shouldBeEqual 200
            body!! shouldBeEqual DetailedVideo(
                id = videoEntity.id,
                url = videoEntity.url,
                title = videoEntity.title,
                description = videoEntity.description,
                uploadedAt = videoEntity.uploadedAt,
                visible = videoEntity.visible,
                crew = listOf(
                    VideoCrew(
                        videoEntity.id,
                        "position",
                        member = SimpleMember(
                            id = memberEntity.id,
                            name = memberEntity.name,
                            nickname = memberEntity.nickname
                        )
                    )
                )
            )
        }
    }
}
