package hu.bsstudio.bssweb.event.entity

import hu.bsstudio.bssweb.video.entity.VideoEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Formula
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.envers.AuditTable
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "event")
@Audited
@AuditTable("event_aud")
@SQLRestriction("deleted_at is null")
data class EventEntity(
    var url: String,
    var title: String,
    var description: String = "",
    var dateFrom: LocalDate = LocalDate.now(),
    var dateTo: LocalDate = LocalDate.now(),
    var visible: Boolean = false,
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID

    @CreationTimestamp
    lateinit var createdAt: Instant

    @UpdateTimestamp
    lateinit var updatedAt: Instant

    /** When the event was deleted, or `null`. A deleted event stays in the table; the restriction above hides it. */
    var deletedAt: Instant? = null

    /** How many videos the event has, deleted ones left out, counted in the same query that loads the event. */
    @NotAudited
    @Formula(
        "(select count(*) from event_video ev join video v on v.id = ev.video_id where ev.event_id = id and v.deleted_at is null)",
    )
    var videoCount: Long = 0

    @NotAudited
    @OneToMany
    @JoinTable(
        name = "event_video",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "video_id")],
    )
    var videos: List<VideoEntity> = emptyList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as EventEntity
        return this.id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString() =
        this::class.simpleName + "(" +
            "url='$url', " +
            "title='$title', " +
            "description='$description', " +
            "dateFrom=$dateFrom, " +
            "dateTo=$dateTo, " +
            "visible=$visible, " +
            "id=$id" +
            ")"
}
