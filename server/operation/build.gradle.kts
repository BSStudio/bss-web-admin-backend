plugins {
    id("hu.bsstudio.gradle.java-library-convention")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.kotlin-convention")
    id("hu.bsstudio.gradle.spotless-convention")
    id("hu.bsstudio.gradle.test-convention")
    id("hu.bsstudio.gradle.jacoco-convention")
    id("hu.bsstudio.gradle.detekt-convention")
    id("hu.bsstudio.gradle.publishing-convention")
    id("hu.bsstudio.gradle.sbom-convention")
}

dependencies {
    api(project(":server:model"))
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-web")
}
