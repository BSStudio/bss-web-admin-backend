package hu.bsstudio.bssweb.videocrew.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.videocrew.client.VideoCrewClient
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode

class RemoveVideoCrewIntegrationTest(
    @Autowired private val client: VideoCrewClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and delete video crew`() {
        val videoEntity = videoRepository.save(DetailedVideoEntity(title = "title"))
        val memberEntity = memberRepository.save(DetailedMemberEntity(url = "url", name = "name"))
        videoCrewRepository.save(VideoCrewEntity(VideoCrewEntityId(videoEntity.id, "position", memberEntity.id)))

        val actual = client.removePosition(videoEntity.id, "position", memberEntity.id)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
            body!! shouldBeEqual
                DetailedVideo(
                    id = videoEntity.id,
                    urls = videoEntity.urls,
                    title = videoEntity.title,
                    description = videoEntity.description,
                    shootingDateStart = videoEntity.shootingDateStart,
                    shootingDateEnd = videoEntity.shootingDateEnd,
                    visible = videoEntity.visible,
                    crew = listOf(),
                    labels = listOf(),
                )
        }
    }
}
