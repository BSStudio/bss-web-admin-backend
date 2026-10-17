package hu.bsstudio.bssweb.label.page

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/** The new label form. The limits are the database's column lengths. */
data class LabelForm(
    @field:NotBlank
    @field:Size(max = NAME_LENGTH)
    val name: String = "",
    @field:NotBlank
    @field:Size(max = DESCRIPTION_LENGTH)
    val description: String = "",
) {
    private companion object {
        private const val NAME_LENGTH = 64
        private const val DESCRIPTION_LENGTH = 256
    }
}
