package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories

class DependencyManagementPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.repositories {
            mavenCentral()
            maven {
                url = project.uri("https://redirector.kotlinlang.org/maven/bootstrap")
            }
        }

        project.dependencies {
            add("implementation", platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
            add("implementation", platform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
        }
    }
}
