package hu.bsstudio.bssweb.event.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "event")
data class SimpleEventEntity(
    override var url: String,
    override var title: String,
    override var description: String = "",
    override var date: LocalDate = LocalDate.now(),
    override var visible: Boolean = false,
) : EventEntity {
    @Id
    @GeneratedValue
    override lateinit var id: UUID
    @CreationTimestamp
    override lateinit var createdAt: Instant
    @UpdateTimestamp
    override lateinit var updatedAt: Instant

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as SimpleEventEntity
        return this.id == other.id
    }

    override fun hashCode() = id.hashCode()
}
