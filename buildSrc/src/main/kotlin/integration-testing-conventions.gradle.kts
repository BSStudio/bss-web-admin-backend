plugins {
    `jvm-test-suite`
}

testing {
    suites {
        val integrationTest by registering(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
                implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
                implementation("org.springframework.boot:spring-boot-starter-test") {
                    exclude(module = "hamcrest") // require developers to use KoTest
                    exclude(module = "org.assertj") // require developers to use KoTest
                    exclude(module = "org.mockito") // require developers to use KoTest
                }
                runtimeOnly("org.junit.platform:junit-platform-launcher")
                implementation("io.kotest:kotest-runner-junit5:5.9.1")
                implementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
            }
        }
    }
}
