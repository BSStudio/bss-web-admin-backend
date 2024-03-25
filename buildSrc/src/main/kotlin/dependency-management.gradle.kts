plugins {
    // should not depend on kotlin-conventions
    // replace with java-conventions
    id("kotlin-conventions")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        // https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#managing-dependencies-dependency-management-plugin-using-in-isolation
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}
