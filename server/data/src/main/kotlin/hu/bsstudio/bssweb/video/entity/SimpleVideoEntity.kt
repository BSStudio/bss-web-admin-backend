package hu.bsstudio.bssweb.video.entity

import jakarta.persistence.MappedSuperclass
import java.util.UUID

@MappedSuperclass
interface SimpleVideoEntity {
    var id: UUID
}
