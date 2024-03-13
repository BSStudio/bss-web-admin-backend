package hu.bsstudio.bssweb.label.service

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.label.mapper.LabelMapper
import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import hu.bsstudio.bssweb.label.repository.LabelRepository
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultLabelServiceTest(
    @MockK private val mockRepository: LabelRepository,
    @MockK private val mockMapper: LabelMapper,
) {
    @InjectMockKs
    private lateinit var underTest: DefaultLabelService

    @Test
    internal fun `should return all labels`() {
        every { mockRepository.findAll() } returns listOf(LABEL_ENTITY)
        every { mockMapper.entityToModel(LABEL_ENTITY) } returns LABEL

        val actual = underTest.findAllLabels()

        actual.shouldContainExactly(LABEL)
    }

    @Test
    internal fun `should insert new label`() {
        every { mockMapper.modelToEntity(CREATE_LABEL) } returns LABEL_ENTITY
        every { mockRepository.save(LABEL_ENTITY) } returns LABEL_ENTITY
        every { mockMapper.entityToModel(LABEL_ENTITY) } returns LABEL

        val actual = underTest.insertLabel(CREATE_LABEL)

        actual shouldBeEqual LABEL
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockRepository.deleteById(LABEL_ID) } returns Unit

        underTest.removeLabel(LABEL_ID)

        verify { mockRepository.deleteById(LABEL_ID) }
    }

    private companion object {
        private val LABEL_ID = mockk<UUID>()
        private val LABEL_ENTITY = mockk<LabelEntity>()
        private val LABEL = mockk<Label>()
        private val CREATE_LABEL = mockk<CreateLabel>()
    }
}
