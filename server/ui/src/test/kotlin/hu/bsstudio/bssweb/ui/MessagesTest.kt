package hu.bsstudio.bssweb.ui

import hu.bsstudio.bssweb.member.common.MemberStatus
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.io.File
import java.util.Properties

/**
 * Keeps the two message files complete and in step: the same keys with the same placeholders, no empty text,
 * every key a template or a controller uses, and none that nothing uses.
 */
internal class MessagesTest {
    private val english = load("messages.properties")
    private val hungarian = load("messages_hu.properties")

    @Test
    internal fun `should have every message in both English and Hungarian`() {
        hungarian.keys shouldBe english.keys
    }

    @Test
    internal fun `should use the same placeholders in both languages`() {
        english.keys
            .filter { placeholders(english.getValue(it)) != placeholders(hungarian[it].orEmpty()) }
            .shouldBeEmpty()
    }

    @Test
    internal fun `should have no empty message`() {
        (english + hungarian.mapKeys { "hu: ${it.key}" }).filterValues(String::isBlank).keys.shouldBeEmpty()
    }

    @Test
    internal fun `should have every message the templates and controllers use`() {
        (usedKeys() - english.keys).shouldBeEmpty()
    }

    @Test
    internal fun `should have no message that nothing uses`() {
        val used = usedKeys()
        english.keys
            .filterNot { it in used || DYNAMIC_PREFIXES.any(it::startsWith) || VALIDATION.matches(it) }
            .shouldBeEmpty()
    }

    @Test
    internal fun `should name every member status`() {
        MemberStatus.entries
            .map { "member.status.$it" }
            .filterNot(english::containsKey)
            .shouldBeEmpty()
    }

    /** Static keys in the templates (`#{key}`), and the keys the controllers pass as string literals. */
    private fun usedKeys(): Set<String> {
        val templates =
            PathMatchingResourcePatternResolver()
                .getResources("classpath*:templates/**/*.html")
                .flatMap { resource -> TEMPLATE_KEY.findAll(resource.getContentAsString(Charsets.UTF_8)).map { it.groupValues[1] } }
        val sections = english.keys.map { it.substringBefore('.') }.toSet()
        val controllers =
            File("src/main/kotlin")
                .walk()
                .filter { it.extension == "kt" }
                .flatMap { file -> KOTLIN_KEY.findAll(file.readText()).map { it.groupValues[1] } }
                .filter { it.substringBefore('.') in sections }
        return (templates + controllers).toSet()
    }

    private fun placeholders(message: String) = PLACEHOLDER.findAll(message).map { it.groupValues[1] }.toSet()

    private fun load(file: String): Map<String, String> =
        Properties()
            .apply {
                MessagesTest::class.java.classLoader
                    .getResourceAsStream(file)!!
                    .reader(Charsets.UTF_8)
                    .use(::load)
            }.let { properties -> properties.stringPropertyNames().associateWith(properties::getProperty) }

    private companion object {
        private val TEMPLATE_KEY = Regex("""#\{([A-Za-z][\w.]*)""")
        private val KOTLIN_KEY = Regex(""""([a-z]+(?:\.[A-Za-z0-9]+)+)"""")
        private val PLACEHOLDER = Regex("""\{(\d+)""")

        /** Keys that templates build at runtime, e.g. `#{${'nav.' + section}}`. */
        private val DYNAMIC_PREFIXES = listOf("nav.", "member.status.", "error.title.", "error.lead.")

        /** Spring looks up validation messages by the constraint's name, e.g. NotBlank or AssertTrue.datesInOrder. */
        private val VALIDATION = Regex("""(NotBlank|NotNull|Size|typeMismatch|AssertTrue\.\w+)""")
    }
}
