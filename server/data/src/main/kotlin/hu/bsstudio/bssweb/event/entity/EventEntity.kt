package hu.bsstudio.bssweb.event.entity

import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@MappedSuperclass
interface EventEntity {
    var id: UUID
    var url: String
    var title: String
    var description: String
    var date: LocalDate
    var visible: Boolean
    @set:CreationTimestamp
    var createdAt: Instant
    @set:UpdateTimestamp
    var updatedAt: Instant
}
