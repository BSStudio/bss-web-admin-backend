package hu.bsstudio.bssweb.video.entity

import java.util.UUID
import javax.persistence.MappedSuperclass

@MappedSuperclass
interface SimpleVideoEntity {
    var id: UUID
}
