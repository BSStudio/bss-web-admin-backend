package hu.bsstudio.bssweb.event.entity

import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "event")
data class DetailedEventEntity(
    override var url: String,
    override var title: String,
    override var description: String = "",
    override var date: LocalDate = LocalDate.now(),
    override var visible: Boolean = false
) : EventEntity {
    @Id
    @GeneratedValue
    override lateinit var id: UUID

    @OneToMany
    @JoinTable(
        name = "event_video",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "video_id")]
    )
    lateinit var videos: List<SimpleVideoEntity>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as DetailedEventEntity
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , url = $url , title = $title , description = $description , date = $date , visible = $visible )"
    }
}
