plugins {
    id("org.springframework.boot")
    id("spring-module-conventions")
    id("jacoco-report-aggregation")
    id("test-report-aggregation")
    war
}

tasks.jar {
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
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    implementation("org.springframework.boot:spring-boot-starter-web")
}
