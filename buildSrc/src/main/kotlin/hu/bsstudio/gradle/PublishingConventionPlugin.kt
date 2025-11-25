package hu.bsstudio.gradle

import org.gradle.api.GradleException
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

        val groupId = project.rootProject.group.toString()
        if (groupId.isBlank()) {
            throw GradleException("Must apply a non-blank group to the root project")
        }

        project.extensions.configure(PublishingExtension::class) {
            repositories {
                maven {
                    name = "GitHubPackages"
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
                    this.groupId = groupId
                }
            }
        }
    }
}