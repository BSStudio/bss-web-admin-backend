plugins {
    id("hu.bsstudio.gradle.spotless-conventions")
    id("hu.bsstudio.gradle.java-library-conventions")
    id("hu.bsstudio.gradle.kotlin-conventions")
    id("hu.bsstudio.gradle.dependency-management")
    id("testing-conventions")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation(libs.kotestAssertionsJson)

    constraints {
        implementation("commons-fileupload:commons-fileupload") {
            version {
                require("[1.6, 2.0[")
                because("CVE-2025-48976")
            }
        }
    }
}
