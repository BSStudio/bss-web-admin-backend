package hu.bsstudio.bssweb.event.entity

import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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
