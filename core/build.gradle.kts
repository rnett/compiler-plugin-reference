plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    kotlin("plugin.serialization")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
}