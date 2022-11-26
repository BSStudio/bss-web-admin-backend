package hu.bsstudio.bssweb.event.entity

import java.time.LocalDate
import java.util.UUID
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "event")
data class EventEntity(
    @Id
    var id: UUID,
    var url: String,
    var title: String,
    var description: String = "",
    var date: LocalDate = LocalDate.now(),
    var visible: Boolean = false,
)
