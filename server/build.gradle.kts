plugins {
    id("hu.bsstudio.gradle.java-convention")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.spotless-convention")
    id("hu.bsstudio.gradle.kotlin-convention")
    id("hu.bsstudio.gradle.spring-app-convention")
    id("hu.bsstudio.gradle.testing-convention")
    `jacoco-report-aggregation`
    `test-report-aggregation`
}

dependencies {
    implementation(project(":server:web"))
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
}

tasks.test {
    finalizedBy(tasks.testAggregateTestReport)
    finalizedBy(tasks.testCodeCoverageReport)
}
