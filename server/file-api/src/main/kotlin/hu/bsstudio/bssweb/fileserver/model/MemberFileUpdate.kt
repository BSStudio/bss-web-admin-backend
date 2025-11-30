package hu.bsstudio.bssweb.fileserver.model

import java.util.UUID

data class MemberFileUpdate(
    val id: UUID,
    val url: String,
)
