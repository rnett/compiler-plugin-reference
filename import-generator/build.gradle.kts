plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

dependencies {
    api(project(":core"))
    implementation("com.squareup:kotlinpoet:1.8.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}