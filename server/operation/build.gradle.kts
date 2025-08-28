plugins {
    id("hu.bsstudio.gradle.spotless-conventions")
    id("hu.bsstudio.gradle.java-library-conventions")
    id("hu.bsstudio.gradle.kotlin-conventions")
    id("hu.bsstudio.gradle.dependency-management")
    id("testing-conventions")
}

dependencies {
    api(project(":server:model"))
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-web")
}
