package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get

class JavaLibraryConventionPlugin : Plugin<Project> {
    /**
     * Configures the given Gradle project as a Java library: applies the Java Library plugin, sets the Java toolchain language version to 25, and makes the `compileOnly` configuration extend from `annotationProcessor`.
     *
     * @param project The target Gradle project to configure.
     */
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaLibraryPlugin::class)

        project.extensions.configure(JavaPluginExtension::class) {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(25))
            }
        }

        project.configurations["compileOnly"].extendsFrom(project.configurations["annotationProcessor"])
    }
}
