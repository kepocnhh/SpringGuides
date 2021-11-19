repositories {
    mavenCentral()
}

plugins {
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("org.springframework.boot") version "2.5.6"
}

group = "guides.spring.gs"
version = "1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    "org.springframework".also { prefix ->
        mapOf(
            "boot" to setOf(
                "starter-web",
                "starter-security"
            ),
            "ldap" to setOf("core"),
            "security" to setOf("ldap")
        ).forEach { (k, v) ->
            v.forEach { postfix ->
                implementation("$prefix.$k:spring-$k-$postfix")
            }
        }
    }
    implementation("com.unboundid:unboundid-ldapsdk")
}
