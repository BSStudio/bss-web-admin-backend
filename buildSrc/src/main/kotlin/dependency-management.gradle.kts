import java.net.URI

plugins {
    id("kotlin-conventions")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
    maven { url = URI.create("https://repo.spring.io/milestone") }
}

dependencyManagement {
    imports {
        // https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#managing-dependencies-dependency-management-plugin-using-in-isolation
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.0-RC3")
    }
}
