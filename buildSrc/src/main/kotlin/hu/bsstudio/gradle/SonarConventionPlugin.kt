package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.sonarqube.gradle.SonarExtension
import org.sonarqube.gradle.SonarQubePlugin
import org.sonarqube.gradle.SonarTask

class SonarConventionPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(SonarQubePlugin::class)
        // project.apply(Sonar)
        project.extensions.configure(SonarExtension::class) {
            properties {
                property("sonar.projectKey", "BSStudio_bss-web-admin-backend")
                property("sonar.organization", "bsstudio")
            }
        }

        project.tasks.named<SonarTask>("sonar") {
            dependsOn(project.tasks.getByName("check"))
        }
    }
}