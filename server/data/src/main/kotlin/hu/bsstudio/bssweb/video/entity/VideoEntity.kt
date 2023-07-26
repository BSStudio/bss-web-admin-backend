package hu.bsstudio.bssweb.video.entity

import jakarta.persistence.MappedSuperclass
import java.time.LocalDate
import java.util.UUID

@MappedSuperclass
interface VideoEntity {
    var id: UUID
    var url: String
    var title: String
    var uploadedAt: LocalDate
    var visible: Boolean
}
