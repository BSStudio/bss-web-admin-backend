package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.TestReportAggregationPlugin
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoReportAggregationPlugin
import org.springframework.boot.gradle.plugin.SpringBootPlugin

class SpringAppConventions : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply {
            apply(SpringModuleConventions::class)
            apply(SpringBootPlugin::class)
            apply(JacocoReportAggregationPlugin::class)
            apply(TestReportAggregationPlugin::class)
        }

        project.tasks.withType<Jar> {
            // if this convention is used we only expect bootJars to be built
            // it will disable the default jar task
            enabled = false
        }

        project.tasks.named("test") {
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
