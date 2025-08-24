repositories {
    mavenCentral()
}

dependencies {
    add("implementation", platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    add("implementation", platform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
    
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
