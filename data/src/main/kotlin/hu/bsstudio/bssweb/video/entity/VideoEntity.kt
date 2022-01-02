package hu.bsstudio.bssweb.video.entity

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "videos")
data class VideoEntity(
    @Id
    override var id: String,
    override var title: String,
    var uploadedAt: LocalDate = LocalDate.now(),
    var visible: Boolean = false,
    var archived: Boolean = false
) : SimpleVideoEntity
