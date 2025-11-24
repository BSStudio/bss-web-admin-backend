package hu.bsstudio.bssweb.label.controller

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import hu.bsstudio.bssweb.label.service.LabelService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import java.util.UUID

@WebMvcTest(LabelController::class)
internal class LabelControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val objectMapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var mockService: LabelService

    @Test
    internal fun `should retrieve all labels`() {
        every { mockService.findAllLabels() } returns LABEL_LIST

        mockMvc
            .get(BASE_URL) {
                with(httpBasic(USERNAME, PASSWORD))
            }.andExpectAll {
                status { isOk() }
                content { objectMapper.writeValueAsString(LABEL_LIST) }
            }
    }

    @Test
    internal fun `should return created on create`() {
        every { mockService.insertLabel(CREATE_LABEL) } returns LABEL

        mockMvc
            .post(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(CREATE_LABEL)
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isCreated() }
                content { objectMapper.writeValueAsString(LABEL) }
                header { string("Location", "http://localhost$BASE_URL/$LABEL_ID") }
            }
    }

    @Test
    internal fun `should return ok after label was removed`() {
        every { mockService.removeLabel(LABEL_ID) } returns Unit

        mockMvc
            .delete("$BASE_URL/$LABEL_ID") {
                with(httpBasic(USERNAME, PASSWORD))
                with(csrf())
            }.andExpectAll {
                status { isNoContent() }
                content { string("") }
            }
    }

    private companion object {
        private const val BASE_URL = "/api/v1/label"
        private const val USERNAME = "user"
        private const val PASSWORD = "password"
        private val LABEL_ID = UUID.randomUUID()
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private val LABEL =
            Label(
                id = LABEL_ID,
                name = NAME,
                description = DESCRIPTION,
            )
        private val CREATE_LABEL = CreateLabel(name = NAME, description = DESCRIPTION)
        private val LABEL_LIST = listOf(LABEL)
    }
}
