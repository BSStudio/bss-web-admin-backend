plugins {
    id("kotlin-conventions")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.3"
extra["mockkVersion"] = "1.12.2"
extra["springdocVersion"] = "1.6.3"

dependencyManagement {
    imports {
        // https://docs.spring.io/spring-boot/docs/2.7.0/gradle-plugin/reference/htmlsingle/#managing-dependencies-dependency-management-plugin-using-in-isolation
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
