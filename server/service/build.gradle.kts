plugins {
    id("hu.bsstudio.gradle.java-library-convention")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.kotlin-convention")
    id("hu.bsstudio.gradle.spotless-convention")
    id("hu.bsstudio.gradle.test-convention")
    id("hu.bsstudio.gradle.jacoco-convention")
    id("hu.bsstudio.gradle.detekt-convention")
}

dependencies {
    api(project(":server:data"))
    api(project(":server:model"))
    api(project(":server:file-api"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework.data:spring-data-jpa")
}
