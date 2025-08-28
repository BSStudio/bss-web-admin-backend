plugins {
    id("hu.bsstudio.gradle.java-convention")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.spotless-convention")
    id("hu.bsstudio.gradle.kotlin-convention")
    id("hu.bsstudio.gradle.spring-app-convention")
    `jacoco-report-aggregation`
    `test-report-aggregation`
}

dependencies {
    implementation(project(":server:web"))
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}

tasks.test {
    finalizedBy(tasks.testAggregateTestReport)
    finalizedBy(tasks.testCodeCoverageReport)
}
