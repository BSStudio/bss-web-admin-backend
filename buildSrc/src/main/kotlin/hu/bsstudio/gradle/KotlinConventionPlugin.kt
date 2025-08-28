package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType

class KotlinConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.spring")
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.jpa")

        project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            compilerOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                extraWarnings = true
                allWarningsAsErrors = true
                jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
            }
        }
    }
}
