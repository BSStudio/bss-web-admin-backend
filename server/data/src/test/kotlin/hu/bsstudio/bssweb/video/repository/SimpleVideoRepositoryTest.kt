package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.DataTest
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class SimpleVideoRepositoryTest(
    @Autowired private val underTest: SimpleVideoRepository
) : DataTest() {

    @Test
    fun `create read delete`() {
        assertThat(underTest.count()).isZero

        val entity = SimpleVideoEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id

        assertThat(underTest.findById(id))
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(
                SimpleVideoEntity(
                    url = URL,
                    title = TITLE,
                    uploadedAt = LocalDate.now(),
                    visible = false
                ).apply {
                    this.id = id
                }
            )

        underTest.deleteById(id)
        assertThat(underTest.findById(id)).isEmpty()
    }

    private companion object {
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
