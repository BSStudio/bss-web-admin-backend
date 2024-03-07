package hu.bsstudio.bssweb.category.repository

import hu.bsstudio.bssweb.category.entity.CategoryEntity
import io.kotest.matchers.longs.shouldBeExactly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryEntityRepositoryTest(
    @Autowired private val underTest: CategoryRepository,
) {
    @Test
    fun `it should have initial data`() {
        underTest.count() shouldBeExactly 25
    }

    @Test
    fun `it should be able to add and remove category`() {
        val saved = underTest.save(CategoryEntity(name = "Test category", description = "Test description"))
        underTest.count() shouldBeExactly 26
        underTest.deleteById(saved.id)
        underTest.count() shouldBeExactly 25
    }
}
