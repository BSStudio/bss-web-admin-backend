package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = [
    "spring.datasource.url=jdbc:tc:postgresql:15.4-alpine:///databasename"
])
class MemberRepositoryTest(
    @Autowired private val underTest: MemberRepository
) {
    @Test
    fun `create read delete`() {
        assertThat(underTest.count()).isZero()

        val entity = DetailedMemberEntity(name = NAME, url = URL)
        val id = underTest.save(entity).id

        assertThat(underTest.findById(id))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(
                DetailedMemberEntity(
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
            )

        underTest.deleteById(id)
        assertThat(underTest.findById(id)).isEmpty()
    }

    private companion object {
        private const val NAME = "Bence Csik"
        private const val URL = "bcsik"
    }
}
