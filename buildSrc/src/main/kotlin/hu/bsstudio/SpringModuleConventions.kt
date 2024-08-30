package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class SpringModuleConventions: Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply(DependencyManagement::class)
    }
}