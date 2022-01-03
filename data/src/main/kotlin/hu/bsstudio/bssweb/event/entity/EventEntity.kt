package hu.bsstudio.bssweb.event.entity

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "events")
data class EventEntity(
    @Id
    var id: String,
    var name: String,
    var description: String = "",
    var date: LocalDate = LocalDate.now(),
    var archived: Boolean = false,
)
