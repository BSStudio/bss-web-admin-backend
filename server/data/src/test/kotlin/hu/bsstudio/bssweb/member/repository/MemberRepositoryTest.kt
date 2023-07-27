package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@TestPropertySource(properties = ["spring.flyway.locations=classpath:db/migration/{vendor}"])
class MemberRepositoryTest(
    @Autowired private val underTest: MemberRepository
) {
    @Test
    fun `create read delete`() {
        assertThat(this.underTest.count()).isZero

        val entity = DetailedMemberEntity(name = NAME, url = URL)
        this.underTest.save(entity)

        val id = entity.id
        val expected = DetailedMemberEntity(
            name = NAME,
            url = URL,
            nickname = "",
            description = "",
            joinedAt = LocalDate.now(),
            role = "",
            status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
            archived = false
        ).apply {
            this.id = id
        }
        assertThat(this.underTest.findById(id)).hasValue(expected)

        this.underTest.deleteById(id)
        assertThat(this.underTest.findById(id)).isEmpty
    }

    private companion object {
        private const val NAME = "Bence Csik"
        private const val URL = "bcsik"
    }
}
