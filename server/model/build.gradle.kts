plugins {
    id("hu.bsstudio.gradle.spotless-conventions")
    id("hu.bsstudio.gradle.java-library-conventions")
    id("hu.bsstudio.gradle.kotlin-conventions")
    id("hu.bsstudio.gradle.dependency-management")
    id("testing-conventions")
}
dependencies {
    api(project(":server:common"))
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation(libs.kotestAssertionsJson)
}
