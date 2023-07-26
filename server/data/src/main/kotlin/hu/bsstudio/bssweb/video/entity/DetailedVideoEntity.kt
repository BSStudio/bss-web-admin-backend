package hu.bsstudio.bssweb.video.entity

import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "video")
data class DetailedVideoEntity(
    override var url: String,
    override var title: String,
    var description: String = "",
    override var uploadedAt: LocalDate = LocalDate.now(),
    override var visible: Boolean = false,
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id")
    var videoCrew: List<DetailedVideoCrewEntity> = listOf()
) : VideoEntity {
    @Id
    @GeneratedValue
    override lateinit var id: UUID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as DetailedVideoEntity
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "DetailedVideoEntity(url='$url', title='$title', description='$description', uploadedAt=$uploadedAt, visible=$visible, id=$id)"
    }
}
