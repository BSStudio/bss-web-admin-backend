package hu.bsstudio.bssweb.ui.user

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.Member
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import java.time.LocalDate
import java.util.UUID

internal class CurrentUserTest {
    @Test
    internal fun `should match the username to a member's URL, ignoring case`() {
        val user = user("sub" to "a1b2", "preferred_username" to "Kovacs-Anna", "name" to "Anna K.")

        CurrentUser.of(user, MEMBERS) shouldBe CurrentUser("Anna K.", "AK", ANNA.id)
    }

    @Test
    internal fun `should match the full name when only one member has it`() {
        val user = user("sub" to "a1b2", "preferred_username" to "bence", "name" to "Nagy Bence")

        CurrentUser.of(user, MEMBERS) shouldBe CurrentUser("Nagy Bence", "NB", BENCE.id)
        CurrentUser.of(user("sub" to "x", "name" to " Nagy   Bence Péter "), MEMBERS).initials shouldBe "NB"
    }

    @Test
    internal fun `should not guess between members with the same name`() {
        val user = user("sub" to "a1b2", "name" to "Kovács Anna")

        CurrentUser.of(user, MEMBERS + ANNA.copy(id = UUID.randomUUID(), url = "anna2")).memberId shouldBe null
    }

    @Test
    internal fun `should fall back to the login name`() {
        val user = user("sub" to "admin")

        CurrentUser.of(user, MEMBERS) shouldBe CurrentUser("admin", "A", null)
    }

    private fun user(vararg attributes: Pair<String, String>) = DefaultOAuth2User(emptyList(), mapOf(*attributes), "sub")

    private companion object {
        private val ANNA =
            Member(
                id = UUID.randomUUID(),
                url = "kovacs-anna",
                name = "Kovács Anna",
                nickname = "",
                description = "",
                joinedAt = LocalDate.of(2024, 9, 1),
                role = "",
                status = MemberStatus.MEMBER,
                archived = false,
            )
        private val BENCE = ANNA.copy(id = UUID.randomUUID(), url = "nagy-bence", name = "Nagy Bence")
        private val MEMBERS = listOf(ANNA, BENCE)
    }
}
