plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
}


tasks.shadowJar {
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:.*"))
        exclude(dependency("org.jetbrains.annotations:.*"))
        exclude(dependency("org.intellij.lang.annotations:.*"))
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
}