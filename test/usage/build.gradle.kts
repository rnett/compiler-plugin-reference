plugins {
    kotlin("jvm") version "1.5.10"
    id("test.gradle-plugin")
}

group = "test"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("test:runtime")
}

