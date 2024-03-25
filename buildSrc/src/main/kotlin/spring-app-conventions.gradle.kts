plugins {
    id("org.springframework.boot")
    id("spring-module-conventions")
    id("jacoco-report-aggregation")
    id("test-report-aggregation")
}

tasks.jar {
    // if this convention is used we only expect bootJars to be built
    // it will disable the default jar task
    enabled = false
}

tasks.test {
    finalizedBy(tasks.named<TestReport>("testAggregateTestReport"))
    finalizedBy(tasks.named<JacocoReport>("testCodeCoverageReport"))
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter")
    // remove
    implementation("org.springframework.boot:spring-boot-starter-web")
}
