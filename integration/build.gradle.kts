plugins {
    id("spring-module-conventions")
    id("ktlint-conventions")
    id("testing-conventions")
}

tasks.jacocoTestCoverageVerification {
    enabled = false
}

tasks.jacocoTestReport {
    enabled = false
}

dependencies {
    api(project(":server:data"))
    api(project(":client"))
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}
