plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}
dependencies {
    api(project(":server:common"))
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}
