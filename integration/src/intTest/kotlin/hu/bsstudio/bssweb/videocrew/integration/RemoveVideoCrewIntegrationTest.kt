package hu.bsstudio.bssweb.videocrew.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.videocrew.client.VideoCrewClient
import org.springframework.beans.factory.annotation.Autowired

class RemoveVideoCrewIntegrationTest(
    @Autowired private val client: VideoCrewClient
) : IntegrationTest()
