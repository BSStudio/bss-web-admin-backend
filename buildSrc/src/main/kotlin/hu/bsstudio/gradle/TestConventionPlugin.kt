package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.invoke
import org.gradle.testing.base.TestingExtension

class TestConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.configure<TestingExtension> {
            suites {
                this.getByName<JvmTestSuite>("test") {
                    useJUnitJupiter()
                    dependencies {
                        implementation("org.springframework.boot:spring-boot-starter-test")
                        runtimeOnly("org.junit.platform:junit-platform-launcher")
                        implementation("io.mockk:mockk-jvm:1.14.5")
                        implementation("com.ninja-squad:springmockk:4.0.2")
                        implementation("io.kotest:kotest-assertions-core-jvm:6.0.0")
                    }
                }
            }
        }
    }
}
