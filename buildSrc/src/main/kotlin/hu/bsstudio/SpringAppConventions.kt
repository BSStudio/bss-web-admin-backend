package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.TestReportAggregationPlugin
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoReportAggregationPlugin
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

class SpringAppConventions : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply {
            apply(SpringModuleConventions::class)
            apply(SpringBootPlugin::class)
            apply(JacocoReportAggregationPlugin::class)
            apply(TestReportAggregationPlugin::class)
        }

        project.tasks.withType<BootJar> {
            archiveClassifier.value("boot")
        }

        project.tasks.named<Test>("test") {
            finalizedBy(
                project.tasks.named("testAggregateTestReport"),
                project.tasks.named("testCodeCoverageReport"),
            )
        }

        project.dependencies {
            add("annotationProcessor", "org.springframework.boot:spring-boot-configuration-processor")
            add("developmentOnly", "org.springframework.boot:spring-boot-devtools")
            add("implementation", "org.springframework.boot:spring-boot-starter")
        }
    }
}
