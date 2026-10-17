package hu.bsstudio.bssweb.member.page

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/** The new member form: the name and the address, the rest is filled in on the edit page. */
data class NewMemberForm(
    @field:NotBlank
    @field:Size(max = MemberForm.NAME_LENGTH)
    val name: String = "",
    @field:NotBlank
    @field:Size(max = MemberForm.URL_LENGTH)
    val url: String = "",
)
