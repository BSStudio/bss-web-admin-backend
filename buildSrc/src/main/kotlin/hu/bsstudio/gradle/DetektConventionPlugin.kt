package hu.bsstudio.gradle

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(DetektPlugin::class)

        project.extensions.configure<DetektExtension> {
            buildUponDefaultConfig = true
            config.setFrom(project.layout.settingsDirectory.file("detekt.yml"))
        }
    }
}
