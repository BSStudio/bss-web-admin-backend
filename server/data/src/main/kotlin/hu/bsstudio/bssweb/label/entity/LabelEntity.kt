package hu.bsstudio.bssweb.label.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.hibernate.annotations.Formula
import java.util.UUID

@Entity
@Table(name = "label")
data class LabelEntity(
    val name: String,
    val description: String,
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID

    /** How many videos have the label, counted in the same query that loads the label. */
    @Formula("(select count(*) from video_label vl where vl.label_id = id)")
    var videoCount: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as LabelEntity
        return this.id == other.id
    }

    override fun hashCode() = id.hashCode()
}
