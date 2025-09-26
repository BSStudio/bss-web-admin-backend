package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType

class KotlinConventionPlugin : Plugin<Project> {
    /**
     * Configures the project with Kotlin plugins and a consistent Kotlin compiler configuration.
     *
     * Applies the Kotlin JVM, Kotlin Spring and Kotlin JPA plugins, and configures all KotlinCompile
     * tasks to enable strict nullability checks, enable extra warnings, treat all warnings as errors,
     * and target JVM 25.
     */
    override fun apply(project: Project) {
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.spring")
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.jpa")

        project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            compilerOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                extraWarnings = true
                allWarningsAsErrors = true
                jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25
            }
        }
    }
}
