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
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-web")
    implementation("tools.jackson.module:jackson-module-kotlin")
    testImplementation(libs.kotestAssertionsJson)
    testImplementation("org.springframework.boot:spring-boot-starter-jackson-test")
}
