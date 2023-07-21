package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

@DataJpaTest
@ExtendWith(MockKExtension::class)
@EnableAutoConfiguration
@EntityScan(basePackages = ["hu.bsstudio.bssweb.member.entity"])
@ContextConfiguration(classes = [MemberRepository::class])
class MemberRepositoryTest {

    @Autowired
    private lateinit var underTest: MemberRepository

    @Test
    fun `create read delete`() {
        val (id) = this.underTest.save(MemberEntity(name = NAME, url = URL))
        val expected = MemberEntity(id = id,
                name = NAME,
                url = URL,
                nickname = "",
                description = "",
                joinedAt = LocalDate.now(),
                role = "",
                status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
                archived = false
        )
        assertThat(this.underTest.findById(id!!)).hasValue(expected)
        this.underTest.deleteById(id)
        assertThat(this.underTest.count()).isZero()
    }

    private companion object {
        private const val NAME = "Bence Csik"
        private const val URL = "bcsik"
    }
}
