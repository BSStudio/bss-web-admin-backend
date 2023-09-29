package hu.bsstudio.bssweb.video.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.video.client.VideoClient
import org.springframework.beans.factory.annotation.Autowired

class DeleteVideoIntegrationTest(
    @Autowired private val client: VideoClient
) : IntegrationTest()
