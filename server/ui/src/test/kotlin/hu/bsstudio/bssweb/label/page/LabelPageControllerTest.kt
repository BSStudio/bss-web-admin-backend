package hu.bsstudio.bssweb.label.page

import com.ninjasquad.springmockk.MockkBean
import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import hu.bsstudio.bssweb.label.service.LabelService
import hu.bsstudio.bssweb.ui.config.UiSecurityConfig
import hu.bsstudio.bssweb.ui.flash.Flash
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.stringContainsInOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID

@WebMvcTest(LabelPageController::class)
@Import(UiSecurityConfig::class)
internal class LabelPageControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var service: LabelService

    @BeforeEach
    internal fun setUp() {
        every { service.findAllLabels() } returns listOf(SPORT, UNIVERSITY_LIFE)
    }

    @Test
    internal fun `should list labels by name`() {
        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
            }.andExpectAll {
                status { isOk() }
                view { name("admin/label/list") }
                content { string(stringContainsInOrder(UNIVERSITY_LIFE.name, SPORT.name)) }
                content { string(containsString("""data-confirm="Delete the label “Sport”? It is removed from every video."""")) }
            }
    }

    @Test
    internal fun `should say when there are no labels`() {
        every { service.findAllLabels() } returns emptyList()

        mockMvc
            .get(BASE_URL) {
                with(oauth2Login())
            }.andExpect {
                content { string(containsString("No labels yet.")) }
            }
    }

    @Test
    internal fun `should create a label`() {
        every { service.insertLabel(CreateLabel(SPORT.name, SPORT.description)) } returns SPORT

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("name", " ${SPORT.name} ")
                param("description", " ${SPORT.description} ")
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("label.flash.created", SPORT.name)) }
            }
    }

    @Test
    internal fun `should keep the input when the name is taken`() {
        every { service.insertLabel(any()) } throws DataIntegrityViolationException("duplicate")

        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("name", SPORT.name)
                param("description", "Matches")
            }.andExpectAll {
                status { isOk() }
                content { string(containsString("Another label already uses this name.")) }
                content { string(containsString("""value="Matches"""")) }
                content { string(containsString("""data-unsaved="pending">""")) }
            }
    }

    @Test
    internal fun `should delete a label`() {
        every { service.removeLabel(SPORT.id) } just runs

        mockMvc
            .post("$BASE_URL/${SPORT.id}/delete") {
                with(oauth2Login())
                with(csrf())
            }.andExpectAll {
                status { is3xxRedirection() }
                redirectedUrl(BASE_URL)
                flash { attribute("flash", Flash("label.flash.deleted", SPORT.name)) }
            }

        verify { service.removeLabel(SPORT.id) }
    }

    @Test
    internal fun `should answer not found for an unknown label`() {
        mockMvc
            .post("$BASE_URL/${UUID.randomUUID()}/delete") {
                with(oauth2Login())
                with(csrf())
            }.andExpect {
                status { isNotFound() }
            }
    }

    private companion object {
        private const val BASE_URL = "/admin/labels"
        private val SPORT = Label(UUID.randomUUID(), "Sport", "Sport events")
        private val UNIVERSITY_LIFE = Label(UUID.randomUUID(), "Egyetemi élet", "University life")
    }
}
