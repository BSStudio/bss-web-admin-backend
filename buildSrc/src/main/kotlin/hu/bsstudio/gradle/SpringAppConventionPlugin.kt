package hu.bsstudio.gradle;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.springframework.boot.gradle.plugin.SpringBootPlugin

class SpringAppConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(SpringBootPlugin::class)
        project.dependencies {
            add("annotationProcessor", "org.springframework.boot:spring-boot-configuration-processor")
            // add("developmentOnly", "org.springframework.boot:spring-boot-devtools")
            add("implementation", "org.springframework.boot:spring-boot-starter")
        }
    }
}
