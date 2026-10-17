package hu.bsstudio.bssweb.label.page

import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.service.LabelService
import hu.bsstudio.bssweb.ui.flash.flash
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.UUID

@Controller
@RequestMapping("/admin/labels")
class LabelPageController(
    private val labelService: LabelService,
) {
    @GetMapping
    fun list(model: Model): String = listPage(model)

    @PostMapping
    fun create(
        @RequestParam name: String,
        @RequestParam description: String,
        model: Model,
        redirect: RedirectAttributes,
    ): String =
        try {
            val label = labelService.insertLabel(CreateLabel(name.trim(), description.trim()))
            redirect.flash("label.flash.created", label.name)
            "redirect:/admin/labels"
        } catch (_: DataIntegrityViolationException) {
            model["name"] = name
            model["description"] = description
            model["error"] = "label.error.duplicate"
            listPage(model)
        }

    @PostMapping("/{labelId}/delete")
    fun delete(
        @PathVariable labelId: UUID,
        redirect: RedirectAttributes,
    ): String {
        val label =
            labelService.findAllLabels().find { it.id == labelId }
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        labelService.removeLabel(labelId)
        redirect.flash("label.flash.deleted", label.name)
        return "redirect:/admin/labels"
    }

    private fun listPage(model: Model): String {
        model["labels"] = labelService.findAllLabels().sortedBy { it.name.lowercase() }
        return "admin/label/list"
    }
}
