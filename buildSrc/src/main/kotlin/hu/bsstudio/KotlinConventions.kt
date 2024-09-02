package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinConventions : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply {
            apply(JavaConventions::class)
            apply("org.jetbrains.kotlin.jvm")
            apply("org.jetbrains.kotlin.plugin.spring")
            apply("org.jetbrains.kotlin.plugin.jpa")
        }

        project.tasks.withType<KotlinCompile> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
                freeCompilerArgs.set(listOf("-Xjsr305=strict"))
            }
        }
    }
}
