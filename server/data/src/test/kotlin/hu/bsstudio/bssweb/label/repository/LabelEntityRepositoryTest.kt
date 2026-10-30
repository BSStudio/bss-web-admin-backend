package hu.bsstudio.bssweb.label.repository

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.repository.VideoRepository
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class LabelEntityRepositoryTest(
    @param:Autowired private val underTest: LabelRepository,
    @param:Autowired private val videoRepository: VideoRepository,
    @param:Autowired private val entityManager: TestEntityManager,
) {
    @Test
    internal fun `it should be able to add and remove label`() {
        val label = LabelEntity(name = "Test label 0", description = "Test label description 0")
        underTest.count() shouldBeExactly 0
        val saved = underTest.save(label)
        underTest.count() shouldBeExactly 1
        underTest.deleteById(saved.id)
        underTest.count() shouldBeExactly 0
    }

    @Test
    internal fun `it should be able to find all labels by name`() {
        val label0 = LabelEntity(name = "Test label 0", description = "Test label description 0")
        val label1 = LabelEntity(name = "Test label 1", description = "Test label description 1")
        underTest.count() shouldBeExactly 0
        val savedLabels = underTest.saveAll(listOf(label0, label1))
        entityManager.flush()
        underTest.count() shouldBeExactly 2
        val actual = underTest.findAllByNameIn(listOf(label0.name, label1.name, "Nonexistent label"))
        actual shouldBe savedLabels
    }

    @Test
    internal fun `it should count the videos of each label when loading them`() {
        val labels = listOf("Popular", "Rare", "Unused").map { underTest.save(LabelEntity(name = it, description = it)) }
        videoRepository.save(VideoEntity(title = "Both").apply { this.labels = labels.take(2) })
        videoRepository.save(VideoEntity(title = "Popular only").apply { this.labels = labels.take(1) })
        entityManager.run {
            flush()
            clear()
        }

        underTest.findAll().associate { it.name to it.videoCount } shouldBe mapOf("Popular" to 2L, "Rare" to 1L, "Unused" to 0L)
    }
}
