package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register


class PublishingConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(MavenPublishPlugin::class)


        project.extensions.configure(PublishingExtension::class) {
            repositories {
                maven {
                    name = "GitHubPackageRegistry"
                    url = project.uri("https://maven.pkg.github.com/BSStudio/bss-web-admin-backend")
                    credentials {
                        username = project.providers.environmentVariable("GPR_USERNAME").orNull
                        password = project.providers.environmentVariable("GPR_PASSWORD").orNull
                    }
                }
            }
            publications {
                register<MavenPublication>(project.name) {
                    from(project.components.getByName("java"))
                    this.groupId = project.rootProject.group.toString()
                    this.artifactId = "${project.rootProject.name}-${project.name}"
                }
            }
        }

        if (project.rootProject.group.toString().isBlank()) {
            project.logger.warn("WARNING: The root project group is not set. Please set it to ensure proper publishing configuration.")
        }
    }
}