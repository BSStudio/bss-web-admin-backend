package hu.bsstudio.bssweb.label.repository

import hu.bsstudio.bssweb.label.entity.LabelEntity
import io.kotest.matchers.longs.shouldBeExactly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LabelEntityRepositoryTest(
    @Autowired private val underTest: LabelRepository,
) {
    @Test
    fun `it should have initial data`() {
        underTest.count() shouldBeExactly 25
    }

    @Test
    fun `it should be able to add and remove category`() {
        val saved = underTest.save(LabelEntity(name = "Test label", description = "Test label description"))
        underTest.count() shouldBeExactly 26
        underTest.deleteById(saved.id)
        underTest.count() shouldBeExactly 25
    }
}
