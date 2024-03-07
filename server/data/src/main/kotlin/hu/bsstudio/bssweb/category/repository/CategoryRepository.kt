package hu.bsstudio.bssweb.category.repository

import hu.bsstudio.bssweb.category.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CategoryRepository : JpaRepository<CategoryEntity, UUID> {
    fun findAllByName(names: List<String>): List<CategoryEntity>
}
