package hu.bsstudio.bssweb.video.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "video")
data class SimpleVideoEntity(
    override var title: String,
    @Column(name = "url")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "video_url", joinColumns = [JoinColumn(name = "video_id")])
    override var urls: List<String> = emptyList(),
    override var description: String = "",
    override var shootingDateStart: LocalDate = LocalDate.now(),
    override var shootingDateEnd: LocalDate = LocalDate.now(),
    override var visible: Boolean = false,
) : VideoEntity {
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
        other as SimpleVideoEntity
        return this.id == other.id
    }

    override fun hashCode() = id.hashCode()
}
