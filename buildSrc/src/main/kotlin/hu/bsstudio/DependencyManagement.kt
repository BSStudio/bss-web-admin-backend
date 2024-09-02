package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories

class DependencyManagement : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaConventions::class)

        project.repositories {
            add(mavenCentral())
        }

        project.dependencies {
            add("api", enforcedPlatform("org.springframework.boot:spring-boot-dependencies:3.3.3"))
            add("api", enforcedPlatform("org.springframework.shell:spring-shell-dependencies:3.3.1"))
            add("api", enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2023.0.3"))
        }
    }
}
