package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.jetbrains.kotlin.allopen.gradle.SpringGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.noarg.gradle.KotlinJpaSubplugin

class KotlinConventions : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply {
            apply(JavaConventions::class)
            apply(KotlinMultiplatformPluginWrapper::class)
            apply(SpringGradleSubplugin::class)
            apply(KotlinJpaSubplugin::class)
        }
    }
}
