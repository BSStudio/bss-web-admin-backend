package hu.bsstudio.bssweb.label.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.label.client.LabelClient
import hu.bsstudio.bssweb.label.entity.LabelEntity
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import java.util.UUID

class DeleteLabelIntegrationTest(
    @Autowired private val client: LabelClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 204 and delete label`() {
        val entity = labelRepository.save(LabelEntity(name = "url", description = "description"))

        val actual = client.removeLabel(entity.id)

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }

    @Test
    internal fun `it should return 204 when label not found`() {
        val actual = client.removeLabel(UUID.fromString("00000000-0000-0000-0000-000000000000"))

        actual.statusCode shouldBeEqual HttpStatusCode.valueOf(204)
    }
}
