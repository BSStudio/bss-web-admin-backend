plugins {
    id("hu.bsstudio.gradle.spotless-conventions")
    id("hu.bsstudio.gradle.java-conventions")
    id("hu.bsstudio.gradle.kotlin-conventions")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.spring-app-conventions")

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