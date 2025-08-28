package hu.bsstudio.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get

class JavaLibraryConventionPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaLibraryPlugin::class)

        project.extensions.configure(JavaPluginExtension::class) {
            targetCompatibility = JavaVersion.VERSION_21
            sourceCompatibility = JavaVersion.VERSION_21
        }

        project.configurations["compileOnly"].extendsFrom(project.configurations["annotationProcessor"])
    }
}