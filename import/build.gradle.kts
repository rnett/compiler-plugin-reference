plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.5.10")
}