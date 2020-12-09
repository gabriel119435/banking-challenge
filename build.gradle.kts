import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.springframework.boot") version "2.4.0" // adds spring boot
    // use maven bom to version dependencies (set by boot)
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    // kotlin("plugin.spring") == id("org.jetbrains.kotlin.plugin.spring")
    kotlin("plugin.spring") version "1.4.21" // open kotlin classes for spring
    kotlin("plugin.jpa") version "1.4.21" // entities with empty constructor
}

group = "some.group.id"
version = "1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation(kotlin("reflect")) // reflection for data-jpa
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("com.h2database:h2")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events(PASSED, FAILED, SKIPPED, STANDARD_OUT, STANDARD_ERROR)
        exceptionFormat = FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showExceptions = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}