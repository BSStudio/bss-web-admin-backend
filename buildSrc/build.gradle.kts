plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.plugin.springBoot)
    implementation(libs.plugin.springDependencyManagement)
    implementation(libs.plugin.ktlint)
    implementation(kotlin("allopen", "1.7.21"))
    implementation(kotlin("gradle-plugin", "1.7.21"))
    // required for kotlin plugin jpa
    implementation(kotlin("reflect", "1.7.21"))
    implementation(kotlin("stdlib-jdk8", "1.7.21"))
    implementation(kotlin("noarg", "1.7.21"))
}
