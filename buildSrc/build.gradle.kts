plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.plugin.springBoot)
    implementation(libs.plugin.springDependencyManagement)
    implementation(libs.plugin.spotless)
    implementation(kotlin("allopen", "2.0.10"))
    implementation(kotlin("gradle-plugin", "2.0.10"))
    // required for kotlin plugin jpa
    implementation(kotlin("reflect", "2.0.10"))
    implementation(kotlin("stdlib-jdk8", "2.0.10"))
    implementation(kotlin("noarg", "2.0.10"))
}
