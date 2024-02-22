package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import io.kotest.matchers.date.shouldBeCloseTo
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.Instant
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest(
    @Autowired private val underTest: MemberRepository,
    @Autowired private val entityManager: TestEntityManager,
) {
    @Test
    fun `create read delete`() {
        underTest.count().shouldBeZero()

        val entity = DetailedMemberEntity(name = NAME, url = URL)
        val id = underTest.save(entity).id

        entityManager.flush()

        val expected =
            DetailedMemberEntity(
                name = NAME,
                url = URL,
                nickname = "",
                description = "",
                joinedAt = LocalDate.now(),
                role = "",
                status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
                archived = false,
            ).apply {
                this.id = id
                this.createdAt = Instant.now()
                this.updatedAt = Instant.now()
            }
        underTest.findById(id) shouldBePresent {
            it.shouldBeEqualToIgnoringFields(expected, ::createdAt, ::updatedAt)
            it.createdAt.shouldBeCloseTo(expected.createdAt, duration = 1.minutes)
            it.updatedAt.shouldBeCloseTo(expected.updatedAt, duration = 1.minutes)
        }

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
    }

    private companion object {
        private const val NAME = "Bence Csik"
        private const val URL = "bcsik"
    }
}
