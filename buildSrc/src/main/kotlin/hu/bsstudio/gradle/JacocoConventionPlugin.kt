package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.math.BigDecimal

class JacocoConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JacocoPlugin::class)

        project.tasks.named<JacocoReport>("jacocoTestReport") {
            dependsOn(project.tasks.named<Test>("test"))
        }

        project.tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
            dependsOn(project.tasks.named<JacocoReport>("jacocoTestReport"))
        }

        project.tasks.named("check") {
            dependsOn(project.tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification"))
        }

        val excluded =
            setOf(
                "**/entity/**",
                "**/exception/**",
                "**/**Config**",
            )

        project.tasks.named<JacocoReport>("jacocoTestReport") {
            // tests are required to run before generating the report
            dependsOn(project.tasks.named<Test>("test"))
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

        project.tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
            // tests are required to run before generating the coverage verification
            dependsOn(project.tasks.named<Test>("test"))
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
    }
}
