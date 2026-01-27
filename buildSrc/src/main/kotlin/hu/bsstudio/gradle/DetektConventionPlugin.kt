package hu.bsstudio.gradle

import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.plugin.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(DetektPlugin::class)

        project.extensions.configure<DetektExtension> {
            config.setFrom(project.layout.settingsDirectory.file("detekt.yml"))
        }
    }
}
