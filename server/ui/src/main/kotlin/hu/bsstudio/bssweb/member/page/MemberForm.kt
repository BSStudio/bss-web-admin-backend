package hu.bsstudio.bssweb.member.page

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

/**
 * The member edit form's fields. Every field has a default, so a field missing from the request fails validation
 * instead of the binding. The limits are the database's column lengths.
 */
data class MemberForm(
    @field:NotBlank
    @field:Size(max = NAME_LENGTH)
    val name: String = "",
    @field:Size(max = TEXT_LENGTH)
    val nickname: String = "",
    @field:NotBlank
    @field:Size(max = URL_LENGTH)
    val url: String = "",
    @field:Size(max = TEXT_LENGTH)
    val role: String = "",
    @field:NotNull
    val status: MemberStatus? = null,
    @param:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull
    val joinedAt: LocalDate? = null,
    @field:Size(max = DESCRIPTION_LENGTH)
    val description: String = "",
    val archived: Boolean = false,
) {
    /** Call only on a valid form. */
    fun toUpdateMember() =
        UpdateMember(
            url = url.trim(),
            name = name.trim(),
            nickname = nickname.trim(),
            description = description.trim(),
            joinedAt = requireNotNull(joinedAt),
            role = role.trim(),
            status = requireNotNull(status),
            archived = archived,
        )

    companion object {
        const val NAME_LENGTH = 250
        const val URL_LENGTH = 250
        private const val TEXT_LENGTH = 250
        private const val DESCRIPTION_LENGTH = 2000

        fun from(member: Member) =
            MemberForm(
                name = member.name,
                nickname = member.nickname,
                url = member.url,
                role = member.role,
                status = member.status,
                joinedAt = member.joinedAt,
                description = member.description,
                archived = member.archived,
            )
    }
}
