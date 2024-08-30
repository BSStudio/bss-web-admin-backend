package hu.bsstudio

import org.gradle.api.Plugin
import org.gradle.api.Project

class IntegrationTestingConventions: Plugin<Project> {
    override fun apply(project: Project) {
//        project.pluginManager.apply(TestingConventions::class)
//
//        val sourceSets = project.extensions.configure<SourceSetContainer>("sourceSets") {}
//        val intTest = sourceSets.create("intTest") {
//            compileClasspath += get("main").output
//            runtimeClasspath += get("main").output
//        }
//
//        val intTestImplementation by project.configurations.getting {
//            extendsFrom(project.configurations.getByName("implementation"))
//        }
//        val intTestRuntimeOnly by project.configurations.getting {
//            extendsFrom(project.configurations.getByName("runtimeOnly"))
//        }
//
//        project.dependencies {
//            add("intTestImplementation","org.springframework.boot:spring-boot-starter-test") {
//                exclude(module = "hamcrest") // require developers to use KoTest
//                exclude(module = "org.assertj") // require developers to use KoTest
//            }
//            add("intTestImplementation","io.kotest:kotest-runner-junit5:5.9.1")
//        }
//
//        val integrationTest = project.tasks.register<Test>("integrationTest") {
//            description = "Runs integration tests."
//            group = "verification"
//
//            testClassesDirs = sourceSets["intTest"].output.classesDirs
//            classpath = sourceSets["intTest"].runtimeClasspath
//            shouldRunAfter("test")
//
//            useJUnitPlatform()
//
//            testLogging {
//                showStandardStreams = true
//            }
//        }
//
//        project.tasks.named("check") { dependsOn(integrationTest) }
    }
}