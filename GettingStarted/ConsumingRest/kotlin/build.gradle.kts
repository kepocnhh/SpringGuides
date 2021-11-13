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
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.6")
}
