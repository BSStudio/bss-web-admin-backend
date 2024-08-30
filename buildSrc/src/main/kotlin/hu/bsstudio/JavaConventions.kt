package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class JavaConventions: Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaLibraryPlugin::class)

        project.extensions.configure(JavaPluginExtension::class) {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }

        project.configurations.apply {
            getByName("compileOnly") {
                extendsFrom(getByName("annotationProcessor"))
            }
        }
    }
}