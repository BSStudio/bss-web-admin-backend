package hu.bsstudio.bssweb.ui.media

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test
import java.util.UUID

internal class MediaLinksTest {
    @Test
    internal fun `should link to the file API's folders`() {
        val media = MediaLinks("https://media.bsstudio.hu/")

        media.enabled shouldBe true
        media.video(ID) shouldBe "https://media.bsstudio.hu/v/$ID/thumbnail"
        media.member(ID) shouldBe "https://media.bsstudio.hu/m/$ID/profile"
        media.media() shouldBeSameInstanceAs media
    }

    @Test
    internal fun `should show no images without a media URL`() {
        MediaLinks("").enabled shouldBe false
    }

    private companion object {
        private val ID = UUID.randomUUID()
    }
}
