package hu.bsstudio.bssweb.video.entity

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "videos")
class DetailedVideoEntity(
    @Id
    override var id: String,
    override var title: String,
    var description: String = "",
    var videoUrl: String? = null,
    var thumbnailUrl: String? = null,
    var uploadedAt: LocalDate = LocalDate.now(),
    var visible: Boolean = false,
    var archived: Boolean = false
) : SimpleVideoEntity {
    fun copy(
        id: String = this.id,
        title: String = this.title,
        description: String = this.description,
        videoUrl: String? = this.videoUrl,
        thumbnailUrl: String? = this.thumbnailUrl,
        uploadedAt: LocalDate = this.uploadedAt,
        visible: Boolean = this.visible,
        archived: Boolean = this.archived,
    ) = DetailedVideoEntity(
        id,
        title,
        description,
        videoUrl,
        thumbnailUrl,
        uploadedAt,
        visible,
        archived,
    )
}
