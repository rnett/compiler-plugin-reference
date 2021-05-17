import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.rnett.compiler-plugin-importer")
}

dependencies{
    implementation("com.rnett.compiler-plugin-reference:import")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.5.0")

    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}

pluginImport{
    packageName = "test.generation"
    inputDirectory = project(":runtime").buildDir.resolve("pluginExport")
    outputDirectory = file("src/main/kotlin")
    dependsOn(":runtime:compileKotlin")
}

