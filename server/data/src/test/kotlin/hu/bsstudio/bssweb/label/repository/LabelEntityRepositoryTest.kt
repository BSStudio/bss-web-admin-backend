package hu.bsstudio.bssweb.label.repository

import hu.bsstudio.bssweb.label.entity.LabelEntity
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class LabelEntityRepositoryTest(
    @Autowired private val underTest: LabelRepository,
    @Autowired private val entityManager: TestEntityManager,
) {
    @Test
    internal fun `it should be able to add and remove label`() {
        underTest.count() shouldBeExactly 0
        val saved = underTest.save(LABEL_0)
        underTest.count() shouldBeExactly 1
        underTest.deleteById(saved.id)
        underTest.count() shouldBeExactly 0
    }

    @Test
    internal fun `it should be able to find all labels by name`() {
        underTest.count() shouldBeExactly 0
        val savedLabels = underTest.saveAll(listOf(LABEL_1, LABEL_2))
        entityManager.flush()
        underTest.count() shouldBeExactly 2
        val actual = underTest.findAllByNameIn(listOf(LABEL_1.name, LABEL_2.name, "Nonexistent label"))
        actual shouldBe savedLabels
    }

    private companion object {
        private val LABEL_0 = LabelEntity(name = "Test label 0", description = "Test label description 0")
        private val LABEL_1 = LabelEntity(name = "Test label 1", description = "Test label description 1")
        private val LABEL_2 = LabelEntity(name = "Test label 2", description = "Test label description 2")
    }
}
