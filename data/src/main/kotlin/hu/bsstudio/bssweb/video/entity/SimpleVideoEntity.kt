package hu.bsstudio.bssweb.video.entity

import javax.persistence.MappedSuperclass

@MappedSuperclass
interface SimpleVideoEntity {
    var id: String
    var title: String
}
