package hu.bsstudio.bssweb.label.mapper

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class LabelMapperTest {
    @InjectMockKs
    private lateinit var underTest: LabelMapper

    @Test
    internal fun `should map entity to model`() {
        val actual = this.underTest.entityToModel(LABEL_ENTITY)

        actual shouldBeEqual LABEL
    }

    @Test
    internal fun `should map model to entity`() {
        val actual = this.underTest.modelToEntity(CREATE_LABEL).apply { this.id = ID }

        actual shouldBeEqual LABEL_ENTITY
    }

    private companion object {
        private val ID = mockk<UUID>()
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private val LABEL_ENTITY =
            LabelEntity(
                name = NAME,
                description = DESCRIPTION,
            ).apply {
                this.id = ID
            }
        private val LABEL =
            Label(
                id = ID,
                name = NAME,
                description = DESCRIPTION,
            )
        private val CREATE_LABEL =
            CreateLabel(
                name = NAME,
                description = DESCRIPTION,
            )
    }
}
