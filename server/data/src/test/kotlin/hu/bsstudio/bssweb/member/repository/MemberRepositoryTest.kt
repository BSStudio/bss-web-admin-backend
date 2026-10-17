package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.date.shouldBeCloseTo
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import java.time.Instant
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class MemberRepositoryTest(
    @param:Autowired private val underTest: MemberRepository,
    @param:Autowired private val entityManager: TestEntityManager,
) {
    @Test
    internal fun `create read delete`() {
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

    @Test
    internal fun `update the status of members in one statement`() {
        val changed = listOf("first", "second").map { underTest.save(DetailedMemberEntity(url = it, name = it)).id }
        val untouched = underTest.save(DetailedMemberEntity(url = "third", name = "third")).id
        entityManager.flush()
        val updatedAt = Instant.parse("2026-10-17T12:00:00Z")

        underTest.updateStatus(changed, MemberStatus.ALUMNI, updatedAt) shouldBe 2

        underTest.findAllById(changed).map { it.status to it.updatedAt } shouldContainOnly listOf(MemberStatus.ALUMNI to updatedAt)
        underTest.findById(untouched) shouldBePresent { it.status shouldBe MemberStatus.MEMBER_CANDIDATE_CANDIDATE }
    }

    private companion object {
        private const val NAME = "Bence Csik"
        private const val URL = "bcsik"
    }
}
