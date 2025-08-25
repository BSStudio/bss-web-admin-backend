plugins {
    jacoco
}

dependencies {
    add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
    add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
    add("testImplementation", "io.mockk:mockk-jvm:1.14.5")
    add("testImplementation", "com.ninja-squad:springmockk:4.0.2")
    add("testImplementation", "io.kotest:kotest-assertions-core-jvm:6.0.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    // generate report after tests run
    finalizedBy(tasks.named<JacocoReport>("jacocoTestReport"))
}

tasks.named("check") {
    // coverage is part of check
    finalizedBy(tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification"))
}

val excluded = setOf(
    "**/entity/**",
    "**/exception/**",
    "**/**Config**",
)

tasks.named<JacocoReport>("jacocoTestReport") {
    // tests are required to run before generating the report
    dependsOn(tasks.named<Test>("test"))
    // require xml report
    reports {
        xml.required.set(true)
    }
    // exclude excluded packages from test report
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(excluded)
                }
            }
        )
    )
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    // tests are required to run before generating the coverage verification
    dependsOn(tasks.named<Test>("test"))
    // set required coverage to 100%
    violationRules {
        rule {
            limit {
                minimum = BigDecimal("1.00")
            }
        }
    }
    // exclude excluded packages from test coverage verification
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(excluded)
                }
            }
        )
    )
}
