package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named

class JavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaLibraryPlugin::class)

        project.extensions.configure(JavaPluginExtension::class) {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }

        project.configurations["compileOnly"].extendsFrom(project.configurations["annotationProcessor"])

        project.tasks.named<Jar>("jar") {
            this.archiveBaseName.set("${project.rootProject.name}-${project.name}")
        }
    }
}
