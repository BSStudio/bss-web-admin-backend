import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.MessageDigest
import java.util.Base64

plugins {
    id("hu.bsstudio.gradle.java-library-convention")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.kotlin-convention")
    id("hu.bsstudio.gradle.spotless-convention")
    id("hu.bsstudio.gradle.testing-convention")
    id("hu.bsstudio.gradle.jacoco-convention")
    id("hu.bsstudio.gradle.detekt-convention")
}

dependencies {
    implementation(project(":server:service"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-security-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(libs.htmx.spring.boot)
    runtimeOnly("org.webjars:webjars-locator-lite")
    runtimeOnly(libs.webjars.htmx)
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-oauth2-client-test")
}

/**
 * Carbon's precompiled stylesheet from npm (`@carbon/styles`), served as `/admin/css/carbon.min.css`. The WebJar
 * on Maven Central is far behind npm, so the build takes the package itself and checks it against npm's
 * integrity hash. Its `@font-face` rules load IBM Plex from IBM's CDN, which the Content-Security-Policy blocks:
 * they are left out, and the type falls back to the system font Carbon lists after Plex.
 */
abstract class CarbonStyles : DefaultTask() {
    @get:Input
    abstract val version: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Inject
    abstract val archives: ArchiveOperations

    @TaskAction
    fun download() {
        val version = version.get()
        val metadata = String(fetch("$REGISTRY/$version"))
        val integrity =
            Regex(""""integrity"\s*:\s*"sha512-([^"]+)"""").find(metadata)?.groupValues?.get(1)
                ?: error("npm lists no sha512 integrity for @carbon/styles $version")
        val tarball = fetch("$REGISTRY/-/styles-$version.tgz")
        val digest = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-512").digest(tarball))
        check(digest == integrity) { "@carbon/styles $version doesn't match npm's integrity hash" }

        val archive = temporaryDir.resolve("styles.tgz").apply { writeBytes(tarball) }
        val css =
            archives
                .tarTree(archives.gzip(archive))
                .matching { include("package/css/styles.min.css") }
                .singleFile
                .readText()
                .replace(FONT_FACE, "")
        val target = outputDir.file("static/admin/css/carbon.min.css").get().asFile
        target.parentFile.mkdirs()
        target.writeText("/*! @carbon/styles $version | Apache-2.0 | https://carbondesignsystem.com */\n$css")
    }

    private fun fetch(url: String): ByteArray {
        val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()
        val response = client.send(HttpRequest.newBuilder(URI(url)).build(), HttpResponse.BodyHandlers.ofByteArray())
        check(response.statusCode() == 200) { "GET $url answered ${response.statusCode()}" }
        return response.body()
    }

    private companion object {
        const val REGISTRY = "https://registry.npmjs.org/@carbon/styles"
        val FONT_FACE = Regex("""@font-face\{[^}]*}""")
    }
}

val carbonStyles =
    tasks.register<CarbonStyles>("carbonStyles") {
        group = "build"
        description = "Downloads Carbon's stylesheet from npm for the admin UI"
        version.set(libs.versions.carbon.styles)
        outputDir.set(layout.buildDirectory.dir("generated/carbon"))
    }

sourceSets.main {
    resources.srcDir(carbonStyles)
}
