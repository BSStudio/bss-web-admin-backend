package hu.bsstudio.bssweb.ui

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.Properties

internal class MessagesTest {
    @Test
    internal fun `should have every message in both English and Hungarian`() {
        keysOf("messages_hu.properties") shouldBe keysOf("messages.properties")
    }

    private fun keysOf(file: String): Set<String> =
        Properties()
            .apply {
                MessagesTest::class.java.classLoader
                    .getResourceAsStream(file)!!
                    .reader(Charsets.UTF_8)
                    .use(::load)
            }.stringPropertyNames()
}
