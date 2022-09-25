plugins {
    id("kotlin-conventions")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.4"
extra["mockkVersion"] = "1.13.1"

dependencyManagement {
    imports {
        // https://docs.spring.io/spring-boot/docs/2.7.3/gradle-plugin/reference/htmlsingle/#managing-dependencies-dependency-management-plugin-using-in-isolation
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
