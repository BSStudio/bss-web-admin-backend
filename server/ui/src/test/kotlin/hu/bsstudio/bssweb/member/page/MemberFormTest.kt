package hu.bsstudio.bssweb.member.page

import hu.bsstudio.bssweb.member.common.MemberStatus
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class MemberFormTest {
    @Test
    internal fun `should not turn a form without a joining date or a status into an update`() {
        shouldThrow<IllegalArgumentException> { MemberForm().toUpdateMember() }
        shouldThrow<IllegalArgumentException> { MemberForm(joinedAt = LocalDate.of(2024, 9, 1)).toUpdateMember() }
        shouldThrow<IllegalArgumentException> { MemberForm(status = MemberStatus.ALUMNI).toUpdateMember() }
    }
}
