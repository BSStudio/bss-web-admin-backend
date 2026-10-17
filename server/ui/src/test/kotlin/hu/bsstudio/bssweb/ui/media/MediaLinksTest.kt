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
        media.video(ID).srcset("avif", null) shouldBe
            "https://media.bsstudio.hu/v/$ID/thumbnail/sd.avif 854w, " +
            "https://media.bsstudio.hu/v/$ID/thumbnail/hd.avif 1280w, " +
            "https://media.bsstudio.hu/v/$ID/thumbnail/fhd.avif 1920w"
        media.member(ID).srcset("webp", "?v=1") shouldBe
            "https://media.bsstudio.hu/m/$ID/profile/m.webp?v=1 854w, " +
            "https://media.bsstudio.hu/m/$ID/profile/l.webp?v=1 1280w, " +
            "https://media.bsstudio.hu/m/$ID/profile/xl.webp?v=1 1920w"
        media.video(ID).fallback(null) shouldBe "https://media.bsstudio.hu/v/$ID/thumbnail/sd.jpeg"
        media.member(ID).fallback("?v=2") shouldBe "https://media.bsstudio.hu/m/$ID/profile/m.jpeg?v=2"
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
