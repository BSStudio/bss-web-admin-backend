package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.base.TestingExtension

class IntegrationTestConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.configure<TestingExtension> {
            suites {
                register<JvmTestSuite>("integrationTest") {
                    useJUnitJupiter()
                    dependencies {
                        implementation("org.springframework.boot:spring-boot-starter-test") {
                            exclude(group = "org.hamcrest")
                            exclude(group = "org.assertj")
                            exclude(group = "org.mockito")
                        }
                        runtimeOnly("org.junit.platform:junit-platform-launcher")
                        implementation("io.kotest:kotest-runner-junit5:6.0.1")
                        implementation("io.kotest:kotest-assertions-core-jvm:6.0.1")
                    }
                }
            }
        }
        project.configurations.named("integrationTestImplementation") {
            extendsFrom(project.configurations.getByName("implementation"))
            extendsFrom(project.configurations.getByName("runtimeOnly"))
        }
        project.afterEvaluate {
            project.tasks.withType<Test>().matching { it.name == "integrationTest" }.configureEach {
                if (System.getenv("CI") != null) {
                    systemProperty("spring.profiles.active", "ci")
                }
                val root = project.rootProject
                inputs
                    .files(
                        root.file("docker-compose.yml"),
                        root.file("docker-compose.ci.yml"),
                        root.file("Dockerfile"),
                        root.fileTree("docker/wiremock"),
                    ).withPropertyName("infrastructure")
                inputs
                    .files(
                        root.file("gradlew"),
                        root.file("gradle.properties"),
                        root.file("settings.gradle.kts"),
                        root.file("client/build.gradle.kts"),
                        root.file("integration/build.gradle.kts"),
                        root.fileTree("gradle"),
                        root.fileTree("buildSrc") { exclude("**/build/**") },
                        root.fileTree("server") { exclude("**/build/**") },
                    ).withPropertyName("dockerBuildContext")
                root.tasks.findByPath(":server:bootJar")?.let { bootJar ->
                    dependsOn(bootJar)
                    inputs.files(bootJar.outputs.files).withPropertyName("appArtifact")
                }
                inputs
                    .property("integrationProfile") {
                        if (System.getenv("CI") != null) "ci" else "default"
                    }
            }
        }
    }
}
