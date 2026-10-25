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
                content { string(stringContainsInOrder("""<td class="num">0</td>""", """<td class="num">37</td>""")) }
                content { string(containsString("""commandfor="delete-${SPORT.id}" aria-label="Delete Sport">Delete</button>""")) }
                content { string(containsString("""<dialog id="delete-${SPORT.id}" aria-labelledby="delete-${SPORT.id}-title"""")) }
                content {
                    string(
                        containsString(
                            """<h2 class="cds--modal-header__heading" id="delete-${SPORT.id}-title">Delete the label “Sport”?</h2>""",
                        ),
                    )
                }
                content { string(containsString("""<p>It is removed from every video. This can’t be undone.</p>""")) }
                content { string(containsString("""action="/labels/${SPORT.id}/delete">""")) }
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
                content {
                    string(
                        containsString("""<small id="name-error" class="field-error">Another label already uses this name.</small>"""),
                    )
                }
                content { string(containsString("""value="Matches"""")) }
                content { string(containsString("""data-unsaved="pending">""")) }
            }
    }

    @Test
    internal fun `should not create a label without a description`() {
        mockMvc
            .post(BASE_URL) {
                with(oauth2Login())
                with(csrf())
                param("name", "n".repeat(65))
            }.andExpectAll {
                status { isOk() }
                view { name("admin/label/list") }
                content { string(containsString("""<small id="name-error" class="field-error">Use at most 64 characters.</small>""")) }
                content { string(containsString("""<small id="description-error" class="field-error">Fill this in.</small>""")) }
            }

        verify(exactly = 0) { service.insertLabel(any()) }
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
        private const val BASE_URL = "/labels"
        private val SPORT = Label(UUID.randomUUID(), "Sport", "Sport events", videoCount = 37)
        private val UNIVERSITY_LIFE = Label(UUID.randomUUID(), "Egyetemi élet", "University life")
    }
}
