package hu.bsstudio.bssweb.video.entity

import java.util.UUID
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
interface SimpleVideoEntity {
    var id: UUID
}
