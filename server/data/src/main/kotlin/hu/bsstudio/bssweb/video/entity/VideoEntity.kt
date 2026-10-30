package hu.bsstudio.bssweb.video.entity

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "video")
data class VideoEntity(
    var title: String,
    @Column(name = "url")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "video_url", joinColumns = [JoinColumn(name = "video_id")])
    var urls: List<String> = emptyList(),
    var description: String = "",
    var shootingDateStart: LocalDate = LocalDate.now(),
    var shootingDateEnd: LocalDate = LocalDate.now(),
    var visible: Boolean = false,
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID

    @OneToMany
    @JoinTable(
        name = "video_label",
        joinColumns = [JoinColumn(name = "video_id")],
        inverseJoinColumns = [JoinColumn(name = "label_id")],
    )
    var labels: List<LabelEntity> = emptyList()

    @OneToMany
    @JoinColumn(name = "video_id")
    var videoCrew: List<DetailedVideoCrewEntity> = emptyList()

    @CreationTimestamp
    lateinit var createdAt: Instant

    @UpdateTimestamp
    lateinit var updatedAt: Instant

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as VideoEntity
        return this.id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString(): String =
        this::class.simpleName + "(" +
            "urls='$urls', " +
            "title='$title', " +
            "description='$description', " +
            "shootingDateStart=$shootingDateStart, " +
            "shootingDateEnd=$shootingDateEnd, " +
            "visible=$visible, " +
            "id=$id" +
            ")"
}
