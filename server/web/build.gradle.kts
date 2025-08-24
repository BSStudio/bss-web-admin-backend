plugins {
    id("spotless-conventions")
    id("java-library-conventions")
    id("kotlin-conventions")
    id("spring-module-conventions")
    id("testing-conventions")
}

dependencies {
    api(project(":server:operation"))
    api(project(":server:service"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation(libs.springdocOpenapiStarterWebmvcUi)
    implementation("org.springframework.data:spring-data-commons")
    testImplementation("org.springframework.security:spring-security-test")

    constraints {
        add("implementation", "org.springframework:spring-webmvc") {
            version {
                require("[6.2.10,)")
            }
            because("CVE-2025-41242")
        }
        add("implementation", "org.apache.commons:commons-lang3") {
            version {
                require("[3.18.0,)")
            }
            because("CVE-2025-48924")
        }
        add("implementation", "com.nimbusds:nimbus-jose-jwt") {
            version {
                require("[9.37.4,)")
            }
            because("CVE-2025-53864")
        }
        add("implementation", "org.apache.tomcat.embed:tomcat-embed-core") {
            version {
                require("[10.1.44,)")
            }
            because("CVE-2025-48989")
        }
    }
}
