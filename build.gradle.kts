group = "hu.bsstudio.web"

plugins {
    `java-platform`
    `maven-publish`
}

dependencies {
    constraints {
        api(project(":client"))
        api(project(":server:operation"))
        api(project(":server:model"))
        api(project(":server:common"))
    }
}

publishing {
    publications {
        create<MavenPublication>("bssWebAdminPlatform") {
            from(components["javaPlatform"])
            this.groupId = project.rootProject.group.toString()
            this.artifactId = "${project.rootProject.name}-${project.name}"
        }
    }
}
