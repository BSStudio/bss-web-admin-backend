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
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("tools.jackson.module:jackson-module-kotlin")
    testImplementation(libs.kotestAssertionsJson)
    testImplementation("org.springframework.boot:spring-boot-starter-jackson-test")

    constraints {
        implementation("commons-fileupload:commons-fileupload") {
            version {
                require("[1.6, 2.0[")
                because("CVE-2025-48976")
            }
        }
    }
}
