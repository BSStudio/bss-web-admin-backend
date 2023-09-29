package hu.bsstudio.bssweb.event.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.event.client.EventClient
import org.springframework.beans.factory.annotation.Autowired

class UpdateEventIntegrationTest(
    @Autowired private val client: EventClient
) : IntegrationTest()
