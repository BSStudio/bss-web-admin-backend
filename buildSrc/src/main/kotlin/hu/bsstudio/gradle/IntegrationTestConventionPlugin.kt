package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import org.gradle.testing.base.TestingExtension

class IntegrationTestConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.configure<TestingExtension> {
            suites {
                register<JvmTestSuite>("integrationTest") {
                    useJUnitJupiter()
                    dependencies {
                        implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
                        implementation("org.springframework.boot:spring-boot-starter-test") {
                            exclude(group = "org.hamcrest")
                            exclude(group = "org.assertj")
                            exclude(group = "org.mockito")
                        }
                        runtimeOnly("org.junit.platform:junit-platform-launcher")
                        implementation("io.kotest:kotest-runner-junit5:6.0.1")
                        implementation("io.kotest:kotest-assertions-core-jvm:6.0.1")
                    }
                }
            }
        }
    }
}
