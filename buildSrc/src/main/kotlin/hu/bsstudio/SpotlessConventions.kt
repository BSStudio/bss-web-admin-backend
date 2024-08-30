package hu.bsstudio

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure


class SpotlessConventions: Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply(SpotlessPlugin::class)

        project.extensions.configure(SpotlessExtension::class) {
            kotlin {
                ktlint()
                toggleOffOn()
            }
            java {
                importOrder()
                removeUnusedImports()
                cleanthat()
                googleJavaFormat()
            }
            kotlinGradle{
                ktlint()
            }
            flexmark {
                target("**/*.md")
                flexmark()
            }
            yaml {
                target("**/*.yml", "**/*.yaml")
                jackson()
                    .yamlFeature("WRITE_DOC_START_MARKER", false)
                    .yamlFeature("MINIMIZE_QUOTES", true)
            }
        }
    }
}