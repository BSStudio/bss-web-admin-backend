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
    implementation(kotlin("allopen", "1.9.22"))
    implementation(kotlin("gradle-plugin", "1.9.22"))
    // required for kotlin plugin jpa
    implementation(kotlin("reflect", "1.9.22"))
    implementation(kotlin("stdlib-jdk8", "1.9.22"))
    implementation(kotlin("noarg", "1.9.22"))
}
