package hu.bsstudio.bssweb.video.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "video")
data class SimpleVideoEntity(
    override var url: String,
    override var title: String,
    override var uploadedAt: LocalDate = LocalDate.now(),
    override var visible: Boolean = false,
) : VideoEntity {
    @Id
    @GeneratedValue
    override lateinit var id: UUID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as SimpleVideoEntity
        return this.id == other.id
    }

    override fun hashCode() = id.hashCode()
}
