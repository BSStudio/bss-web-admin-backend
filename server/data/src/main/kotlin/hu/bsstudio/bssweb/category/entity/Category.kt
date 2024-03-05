package hu.bsstudio.bssweb.category.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.UUID

@Entity
@Table(name = "category")
data class Category(
    val name: String,
    val description: String,
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Category
        return this.id == other.id
    }

    override fun hashCode() = id.hashCode()
}
