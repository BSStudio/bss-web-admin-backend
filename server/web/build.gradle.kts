plugins {
    id("hu.bsstudio.gradle.spotless-conventions")
    id("hu.bsstudio.gradle.java-library-conventions")
    id("hu.bsstudio.gradle.kotlin-conventions")
    id("hu.bsstudio.gradle.dependency-management")
    id("testing-conventions")
}

dependencies {
    api(project(":server:operation"))
    api(project(":server:service"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation(libs.springdocOpenapiStarterWebmvcUi)
    implementation("org.springframework.data:spring-data-commons")
    testImplementation("org.springframework.security:spring-security-test")

    constraints {
        implementation("com.nimbusds:nimbus-jose-jwt") {
            version {
                require("[9.37.4,)")
            }
            because("CVE-2025-53864")
        }
    }
}
