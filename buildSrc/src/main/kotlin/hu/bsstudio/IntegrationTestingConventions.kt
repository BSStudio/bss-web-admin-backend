package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.the

class IntegrationTestingConventions : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(TestingConventions::class)

        project.extensions.configure<SourceSetContainer> {
            create("intTest") {
                compileClasspath += getByName("main").output
                runtimeClasspath += getByName("main").output
            }
        }

        project.configurations.getByName("intTestImplementation")
            .extendsFrom(project.configurations.getByName("implementation"))

        project.configurations.getByName("intTestRuntimeOnly")
            .extendsFrom(project.configurations.getByName("runtimeOnly"))


        project.dependencies {
            add("intTestImplementation", "org.springframework.boot:spring-boot-starter-test") {
                exclude(module = "hamcrest") // require developers to use KoTest
                exclude(module = "org.assertj") // require developers to use KoTest
            }
            add("intTestImplementation", "io.kotest:kotest-runner-junit5:5.9.1")
            add("intTestImplementation", "io.kotest:kotest-assertions-core-jvm:5.9.1")
        }

        val integrationTest = project.tasks.register("integrationTest", Test::class) {
            description = "Runs integration tests."
            group = "verification"

            val intTest = project.the<SourceSetContainer>()["intTest"]
            testClassesDirs = intTest.output.classesDirs
            classpath = intTest.runtimeClasspath
            shouldRunAfter("test")

            useJUnitPlatform()
        }

        project.tasks.named("check") { dependsOn(integrationTest) }
    }
}
