package hu.bsstudio.bssweb.video.entity

import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "video")
data class VideoEntity(
    @Id
    override var id: UUID,
    var url: String,
    var title: String,
    var uploadedAt: LocalDate = LocalDate.now(),
    var visible: Boolean = false
) : SimpleVideoEntity
