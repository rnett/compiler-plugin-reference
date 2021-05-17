import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    id("test.gradle-plugin")
}

group = "test"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies{
    implementation("test:runtime")
}

