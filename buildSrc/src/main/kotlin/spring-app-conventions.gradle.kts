plugins {
    id("org.springframework.boot")
    id("dependency-management")
    id("jacoco-report-aggregation")
    id("test-report-aggregation")
}

tasks.named<Test>("test") {
    finalizedBy(tasks.named<TestReport>("testAggregateTestReport"))
    finalizedBy(tasks.named<JacocoReport>("testCodeCoverageReport"))
}

dependencies {
    add("annotationProcessor", "org.springframework.boot:spring-boot-configuration-processor")
    add("developmentOnly", "org.springframework.boot:spring-boot-devtools")
    add("implementation", "org.springframework.boot:spring-boot-starter")
}
