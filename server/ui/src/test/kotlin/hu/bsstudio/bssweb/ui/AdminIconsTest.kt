package hu.bsstudio.bssweb.ui

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal class AdminIconsTest {
    @Test
    internal fun `should include every generated browser and device icon`() {
        val icons =
            listOf(
                "favicon.svg",
                "favicon-96x96.png",
                "favicon.ico",
                "apple-touch-icon.png",
                "web-app-manifest-192x192.png",
                "web-app-manifest-512x512.png",
                "site.webmanifest",
                "logo.svg",
                "logo-horizontal.svg",
            )

        icons.forEach { name -> assertTrue(icon(name).exists(), "$name is present") }

        val layout = readResource("templates/admin/layout.html")
        listOf("favicon.svg", "favicon-96x96.png", "favicon.ico", "apple-touch-icon.png", "site.webmanifest", "logo-horizontal.svg")
            .forEach { name -> assertTrue(layout.contains(name), "layout references $name") }
    }

    @Test
    internal fun `should provide the expected png and ico sizes`() {
        assertEquals(96 to 96, pngDimensions("favicon-96x96.png"))
        assertEquals(180 to 180, pngDimensions("apple-touch-icon.png"))
        assertEquals(192 to 192, pngDimensions("web-app-manifest-192x192.png"))
        assertEquals(512 to 512, pngDimensions("web-app-manifest-512x512.png"))

        val ico = ByteBuffer.wrap(icon("favicon.ico").inputStream.readBytes()).order(ByteOrder.LITTLE_ENDIAN)
        assertEquals(0, ico.getShort(0).toInt(), "ICO reserved field")
        assertEquals(1, ico.getShort(2).toInt(), "ICO image type")
        assertEquals(3, ico.getShort(4).toInt(), "ICO image count")
        val sizes = (0..2).map { index -> (ico.get(6 + index * 16).toInt() and 0xff).let { if (it == 0) 256 else it } }
        assertEquals(listOf(16, 32, 48), sizes.sorted())
    }

    @Test
    internal fun `should declare the android manifest icons`() {
        val manifest = readResource("static/admin/icons/site.webmanifest")
        assertTrue(manifest.contains("\"name\": \"BSS admin\""))
        assertTrue(manifest.contains("\"sizes\": \"192x192\""))
        assertTrue(manifest.contains("\"sizes\": \"512x512\""))
        assertTrue(manifest.contains("\"purpose\": \"maskable\""))
    }

    @Test
    internal fun `square logo frame should reach the canvas edges`() {
        val logo = readResource("static/admin/icons/logo.svg")
        assertTrue(logo.contains("viewBox=\"0 0 48 48\""))
        assertTrue(logo.contains("<rect x=\"1.5\" y=\"1.5\" width=\"45\" height=\"45\" rx=\"6.5\""))
        assertTrue(logo.contains("stroke-width=\"3\""))
    }

    @Test
    internal fun `rectangular logo should frame only BSS with matching top and side padding`() {
        val logo = readResource("static/admin/icons/logo-horizontal.svg")
        assertTrue(logo.contains("viewBox=\"0 0 48 28\""))
        assertTrue(logo.contains("<rect x=\"1.5\" y=\"1.5\" width=\"45\" height=\"25\" rx=\"4.5\""))
        assertTrue(logo.contains("translate(8.4598 20)"))
        assertTrue(!logo.contains("admin"))
        assertTrue(logo.contains("stroke-width=\"3\""))
    }

    private fun pngDimensions(name: String): Pair<Int, Int> {
        val png = ByteBuffer.wrap(icon(name).inputStream.readBytes()).order(ByteOrder.BIG_ENDIAN)
        assertEquals(0x89504e47.toInt(), png.getInt(0), "$name PNG signature")
        return png.getInt(16) to png.getInt(20)
    }

    private fun icon(name: String) = ClassPathResource("static/admin/icons/$name")

    private fun readResource(path: String) = ClassPathResource(path).inputStream.bufferedReader().use { it.readText() }
}
