package hu.bsstudio.bssweb.fileserver.model

import java.util.UUID

data class VideoFileUpdate(
    val id: UUID,
    val urls: List<String>,
)
