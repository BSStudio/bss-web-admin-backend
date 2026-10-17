package hu.bsstudio.bssweb.member.page

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import java.time.LocalDate

/** The member edit form's fields. Only the checkbox has a default, because browsers leave it out when unchecked. */
data class MemberForm(
    val name: String,
    val nickname: String,
    val url: String,
    val role: String,
    val status: MemberStatus,
    val joinedAt: LocalDate,
    val description: String,
    val archived: Boolean = false,
) {
    fun toUpdateMember() =
        UpdateMember(
            url = url.trim(),
            name = name.trim(),
            nickname = nickname.trim(),
            description = description.trim(),
            joinedAt = joinedAt,
            role = role.trim(),
            status = status,
            archived = archived,
        )

    companion object {
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
