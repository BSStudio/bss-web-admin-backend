package hu.bsstudio.bssweb.fileserver.model

import java.util.UUID

data class FileUpdate(
    val id: UUID,
    val url: String
)
