package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.math.BigDecimal

class TestingConventions : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply {
            apply(DependencyManagement::class)
            apply(JacocoPlugin::class)
        }

        project.dependencies {
            add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
        }

        val excluded =
            setOf(
                "**/entity/**",
                "**/exception/**",
                "**/**Config**",
            )

        val jacocoTestReport =
            project.tasks.withType<JacocoReport> {
                // require xml report
                reports {
                    xml.required.set(true)
                }
                // exclude excluded packages from test report
                classDirectories.setFrom(
                    project.files(
                        classDirectories.files.map {
                            project.fileTree(it) {
                                exclude(excluded)
                            }
                        },
                    ),
                )
            }

        val jacocoTestCoverageVerification =
            project.tasks.withType<JacocoCoverageVerification> {
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
                    project.files(
                        classDirectories.files.map {
                            project.fileTree(it) {
                                exclude(excluded)
                            }
                        },
                    ),
                )
            }

        project.tasks.named("check") {
            // coverage is part of check
            finalizedBy(jacocoTestCoverageVerification)
        }

        val test =
            project.tasks.withType<Test> {
                useJUnitPlatform()
                // generate report after tests run
                finalizedBy(jacocoTestReport)
            }

        jacocoTestReport.configureEach { dependsOn(test) }
        jacocoTestCoverageVerification.configureEach { dependsOn(test) }
    }
}
