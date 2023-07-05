package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTestBase
import hu.bsstudio.bssweb.member.service.MemberService
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreateMemberTest(
    @Autowired private val memberService: MemberService
): IntegrationTestBase() {

    @Test
    fun testName() {
        assertThat("").isBlank()
    }
}
