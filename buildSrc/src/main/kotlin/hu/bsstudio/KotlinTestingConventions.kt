package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class KotlinTestingConventions : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(TestingConventions::class)
        project.dependencies {
            add("testImplementation", "io.mockk:mockk-jvm:1.13.12")
            add("testImplementation", "com.ninja-squad:springmockk:4.0.2")
            add("testImplementation", "io.kotest:kotest-assertions-core-jvm:5.9.1")
        }
    }
}
