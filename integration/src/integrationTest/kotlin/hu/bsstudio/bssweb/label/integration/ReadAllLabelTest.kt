package hu.bsstudio.bssweb.label.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.label.client.LabelClient
import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.label.model.Label
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode

internal class ReadAllLabelTest(
    @param:Autowired private val client: LabelClient,
) : IntegrationTest() {
    @Test
    internal fun `it should return 200 and empty list`() {
        val actual = client.getAllLabels()

        assertSoftly(actual) {
            body.shouldBeEmpty()
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        }
    }

    @Test
    internal fun `it should return 200 and list with 1 label`() {
        val entity = labelRepository.save(LabelEntity(name = NAME, description = DESCRIPTION))

        val actual = client.getAllLabels()

        assertSoftly(actual) {
            body.shouldContainExactly(
                Label(
                    id = entity.id,
                    name = NAME,
                    description = DESCRIPTION,
                ),
            )
            statusCode shouldBeEqual HttpStatusCode.valueOf(200)
        }
    }

    private companion object {
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
    }
}
