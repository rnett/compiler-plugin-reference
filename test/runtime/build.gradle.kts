import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.rnett.compiler-plugin-referenceer")
}

dependencies{
    implementation("com.rnett.compiler-plugin-reference:export")
}

