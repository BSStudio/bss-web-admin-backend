package hu.bsstudio.bssweb.video.entity

import jakarta.persistence.ElementCollection
import jakarta.persistence.FetchType
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@MappedSuperclass
interface VideoEntity {
    var id: UUID

    var title: String
    @get:ElementCollection(fetch = FetchType.EAGER)
    var urls: List<String>
    var description: String
    var shootingDateStart: LocalDate
    var shootingDateEnd: LocalDate
    var visible: Boolean

    @set:CreationTimestamp
    var createdAt: Instant

    @set:UpdateTimestamp
    var updatedAt: Instant
}
