package hu.bsstudio.bssweb.member.integration

import hu.bsstudio.bssweb.IntegrationTestBase
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.controller.MemberController
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreateMemberIntegrationTest(
        @Autowired private val memberController: MemberController,
        @Autowired private val memberRepository: MemberRepository
): IntegrationTestBase() {

    @Test
    fun testName() {
        val memberEntity = memberRepository.save(MemberEntity(url = URL, name = NAME))

        val actual = memberController.getMemberById(memberEntity.id)

        val expected = Member(
            id = memberEntity.id,
            url = URL,
            name = NAME,
            nickname = "",
            description = "",
            joinedAt = memberEntity.joinedAt,
            role = "",
            status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE,
            archived = false
        )
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        private const val URL = "bcsik"
        private const val NAME = "Bence Csik"
    }
}
