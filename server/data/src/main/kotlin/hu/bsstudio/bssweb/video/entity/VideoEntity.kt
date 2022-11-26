package hu.bsstudio.bssweb.video.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

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
