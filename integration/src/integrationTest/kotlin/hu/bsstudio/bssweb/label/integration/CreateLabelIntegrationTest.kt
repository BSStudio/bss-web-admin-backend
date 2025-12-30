package hu.bsstudio.bssweb.label.integration

import hu.bsstudio.bssweb.IntegrationTest
import hu.bsstudio.bssweb.label.client.LabelClient
import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpServerErrorException
import java.net.URI

class CreateLabelIntegrationTest : IntegrationTest() {
    @Autowired
    private lateinit var client: LabelClient

    @Value("\${bss.client.url}")
    private lateinit var url: String

    @Test
    internal fun `it should return 201 and label`() {
        val actual = client.createLabel(CREATE_LABEL)

        assertSoftly(actual) {
            statusCode shouldBeEqual HttpStatusCode.valueOf(201)
            body!! shouldBeEqual
                Label(
                    id = actual.body!!.id,
                    name = CREATE_LABEL.name,
                    description = CREATE_LABEL.description,
                )
            headers.location!! shouldBeEqual URI.create("$url/api/v1/label/${actual.body!!.id}")
        }
    }

    @Test
    internal fun `it should retun 500 when duplicate urls were specified`() {
        labelRepository.save(LabelEntity(name = CREATE_LABEL.name, description = CREATE_LABEL.description))

        shouldThrow<HttpServerErrorException.InternalServerError> {
            client.createLabel(CREATE_LABEL)
        } should { it.responseBodyAsString shouldContain ""","status":500,"error":"Internal Server Error"""" }
    }

    private companion object {
        private val CREATE_LABEL =
            CreateLabel(
                name = "name",
                description = "description",
            )
    }
}
